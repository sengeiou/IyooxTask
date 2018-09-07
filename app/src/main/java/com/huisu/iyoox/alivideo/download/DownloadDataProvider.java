package com.huisu.iyoox.alivideo.download;

import android.content.Context;

import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo.Status;
import com.huisu.iyoox.alivideo.utils.DownloadSaveInfoUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Mulberry
 * create on 2018/4/16.
 */

public class DownloadDataProvider {

    private volatile static DownloadDataProvider instance;
    private AliyunDownloadManager downloadManager;
    private WeakReference<Context> contextWeakReference;
    private ArrayList<AliyunDownloadMediaInfo> aliyunDownloadMediaInfos;
    private DownloadSaveInfoUtil downloadSaveInfoUtil;

    public DownloadDataProvider(Context context) {
        contextWeakReference = new WeakReference<Context>(context);
        downloadManager = AliyunDownloadManager.getInstance(contextWeakReference.get());
        downloadSaveInfoUtil = new DownloadSaveInfoUtil(downloadManager.getSaveDir());
    }

    public static DownloadDataProvider getSingleton(Context context) {
        if (instance == null) {
            synchronized (DownloadDataProvider.class) {
                if (instance == null) {
                    instance = new DownloadDataProvider(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取所有的下载数据
     * @return
     */
    public ArrayList<AliyunDownloadMediaInfo> getAllDownloadMediaInfo(){
        aliyunDownloadMediaInfos = new ArrayList<>();
        if (downloadSaveInfoUtil.getAlivcDownloadeds() != null){
            aliyunDownloadMediaInfos.addAll(downloadSaveInfoUtil.getAlivcDownloadeds());
            deleteDumpData();
        }

        for (AliyunDownloadMediaInfo info :aliyunDownloadMediaInfos){
            if (info.getStatus() != Status.Complete){
                downloadManager.addDownloadMedia(info);
            }
        }

        return aliyunDownloadMediaInfos;
    }

    /**
     * 数据去重
     * @return
     */
    public void deleteDumpData(){
        Set set  =   new  HashSet();
        List newList  =   new  ArrayList();
        for  (Iterator iter = aliyunDownloadMediaInfos.iterator(); iter.hasNext();)   {
            Object element  =  iter.next();
            if  (set.add(element)){
                newList.add(element);
            }
        }

        aliyunDownloadMediaInfos.clear();
        aliyunDownloadMediaInfos.addAll(newList);
    }

    /**
     * 添加新的下载任务
     * @param aliyunDownloadMediaInfo
     * @return
     */
    public void addDownloadMediaInfo(AliyunDownloadMediaInfo aliyunDownloadMediaInfo){
        if (hasAdded(aliyunDownloadMediaInfo)){
            return;
        }
        if (aliyunDownloadMediaInfos != null) {
            aliyunDownloadMediaInfos.add(aliyunDownloadMediaInfo);
            downloadSaveInfoUtil.writeDownloadingInfo(aliyunDownloadMediaInfo);
        }
    }

    /**
     * 判断是否已经存在
     * @param info
     * @return
     */
    public boolean hasAdded(AliyunDownloadMediaInfo info) {
        for (AliyunDownloadMediaInfo downloadMediaInfo : aliyunDownloadMediaInfos) {
            if (info.getFormat().equals(downloadMediaInfo.getFormat()) &&
                    info.getQuality().equals(downloadMediaInfo.getQuality()) &&
                    info.getVid().equals(downloadMediaInfo.getVid()) &&
                    info.isEncripted() == downloadMediaInfo.isEncripted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除已经下载的数据
     * @param aliyunDownloadMediaInfo
     * @return
     */
    public void deleteDownloadMediaInfo(AliyunDownloadMediaInfo aliyunDownloadMediaInfo){
        if (aliyunDownloadMediaInfos != null){
            downloadManager.removeDownloadMedia(aliyunDownloadMediaInfo);
            aliyunDownloadMediaInfos.remove(aliyunDownloadMediaInfo);
            downloadSaveInfoUtil.deleteInfo(aliyunDownloadMediaInfo);
        }
    }

    /**
     * 删除所有视频
     * @return
     */
    public void deleteAllDownloadInfo(ArrayList<AlivcDownloadMediaInfo> alivcDownloadMediaInfos){
        if (aliyunDownloadMediaInfos != null){
            aliyunDownloadMediaInfos.clear();
        }

        for (AlivcDownloadMediaInfo info :alivcDownloadMediaInfos){
            deleteDownloadMediaInfo(info.getAliyunDownloadMediaInfo());
        }

    }


}

package com.github.tvbox.osc.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author pj567
 * @date :2021/1/12
 * @description:
 */
public class LiveChannelItem implements Serializable, Cloneable {
    /**
     * channelIndex : 频道索引号
     * channelNum : 频道名称
     * channelSourceNames : 频道源名称
     * channelUrls : 频道源地址
     * sourceIndex : 频道源索引
     * sourceNum : 频道源总数
     */
    private int channelIndex;
    private int channelNum;
    private String channelName;
    private ArrayList<String> channelSourceNames = new ArrayList<>();
    private ArrayList<String> channelUrls = new ArrayList<>();
    public int sourceIndex = 0;
    public int sourceNum = 0;
    public boolean include_back = false;
    public boolean isCollected;

    public void setinclude_back(boolean include_back) {
        this.include_back = include_back;
    }

    public boolean getinclude_back() {
        return include_back;
    }

    public void setChannelIndex(int channelIndex) {
        this.channelIndex = channelIndex;
    }

    public int getChannelIndex() {
        return channelIndex;
    }

    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public ArrayList<String> getChannelUrls() {
        return channelUrls;
    }

    public void setChannelUrls(ArrayList<String> channelUrls) {
        this.channelUrls = channelUrls;
        sourceNum = channelUrls.size();
    }
    public void preSource() {
        sourceIndex--;
        if (sourceIndex < 0) sourceIndex = sourceNum - 1;
    }
    public void nextSource() {
        sourceIndex++;
        if (sourceIndex > channelUrls.size()) {
            sourceIndex = 0;
        }
        if (sourceIndex == sourceNum) sourceIndex = 0;
    }

    public void setSourceIndex(int sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    public int getSourceIndex() {
        return sourceIndex;
    }

    public String getUrl() {

        return channelUrls.get(sourceIndex);
    }

    public boolean isForceTv() {
        return getUrl().toLowerCase().startsWith("p") || getUrl().equals("mitv");
    }

    public int getSourceNum() {
        return sourceNum;
    }

    public ArrayList<String> getChannelSourceNames() {
        return channelSourceNames;
    }

    public void setChannelSourceNames(ArrayList<String> channelSourceNames) {
        this.channelSourceNames = channelSourceNames;
    }

    public String getSourceName() {
        return channelSourceNames.get(sourceIndex);
    }

    @NonNull
    @Override
    public LiveChannelItem clone() {
        try {
            return (LiveChannelItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiveChannelItem that = (LiveChannelItem) o;
        return channelNum == that.channelNum && Objects.equals(channelName, that.channelName) && Objects.equals(channelSourceNames, that.channelSourceNames) && Objects.equals(channelUrls, that.channelUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelNum, channelName, channelSourceNames, channelUrls);
    }
}
package com.syc.blog.model;

import java.io.Serializable;

public class ZimgResponse implements Serializable {
    private boolean ret;
    private ZimgInfo info;

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public ZimgInfo getInfo() {
        return info;
    }

    public void setInfo(ZimgInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ZimgResponse{" +
                "ret=" + ret +
                ", info=" + info +
                '}';
    }

    public class ZimgInfo implements Serializable {
        private String md5;
        private long size;

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "ZimgInfo{" +
                    "md5='" + md5 + '\'' +
                    ", size=" + size +
                    '}';
        }
    }
}

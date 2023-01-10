package com.example.demo_bckj.model.bean;

import java.util.List;

/**
 * @author ZJL
 * @date 2023/1/9 16:50
 * @des 用户在线实体类
 * @updateAuthor
 * @updateDes
 */

// "code": 0,
//         "message": "OK",
//         "data": {
//  "type": "off_line", // 弹窗类型，可选值（off_line,auth）
//          "closeable": false, // 弹窗是否可关
//          "message": "未成年用户只能在周五、周六、周日及法定节假日的20：00-21：00进行游戏，其他时间禁止游戏。" // 弹窗内容
// },
//         "errors": []
public class OnlineBean {
    private int code;
    private String message;
    private Data data;
    private List<?> errors;

    public static class Data{
        private String type;
        private boolean closeable;
        private String message;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isCloseable() {
            return closeable;
        }

        public void setCloseable(boolean closeable) {
            this.closeable = closeable;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "type='" + type + '\'' +
                    ", closeable=" + closeable +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "OnlineBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}

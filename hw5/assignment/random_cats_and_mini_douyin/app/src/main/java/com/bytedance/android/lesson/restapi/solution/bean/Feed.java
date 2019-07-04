package com.bytedance.android.lesson.restapi.solution.bean;

/**
 * @author Xavier.S
 * @date 2019.01.20 14:18
 */
public class Feed {

    // TODO-C2 (1) Implement your Feed Bean here according to the response json
    private String student_id;
    private String user_name;
    private String image_url;
    private String _id;
    private String video_url;
    private String createAt;
    private String updateAt;
    private int __v;

    public String getStudent_id() {
        return student_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String get_id() {
        return _id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public int get__v() {
        return __v;
    }
}

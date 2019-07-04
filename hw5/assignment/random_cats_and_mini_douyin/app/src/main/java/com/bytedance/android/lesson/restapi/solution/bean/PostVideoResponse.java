package com.bytedance.android.lesson.restapi.solution.bean;

import okhttp3.MultipartBody;

/**
 * @author Xavier.S
 * @date 2019.01.18 17:53
 */
public class PostVideoResponse {

    // TODO-C2 (3) Implement your PostVideoResponse Bean here according to the response json
    // response
    // {
    //    "result": {},
    //    "url": "https://lf1-hscdn-tos.pstatp
    //    .com/obj/developer-baas/baas/tt7217xbo2wz3cem41/a8efa55c5c22de69_1560563154288.mp4",
    //    "success": true
    //}

    private Item result;
    private String url;
    private Boolean success;
    private String error;

    public Item getResult() {
        return result;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getError(){
        return error;
    }

    public class  Item{

        private String student_id;
        private String user_name;
        private String image_url;
        private String video_url;

        public String getStudent_id() {
            return student_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getImage_url() {
            return image_url;
        }

        public String getVideo_url() {
            return video_url;
        }
    }
}

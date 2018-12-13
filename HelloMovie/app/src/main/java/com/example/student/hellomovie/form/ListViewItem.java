package com.example.student.hellomovie.form;

/**
 * Created by student on 2018-12-13.
 */

public class ListViewItem {
    String title;
    String date;
    int img_id;

    public ListViewItem(String title, String date, int img_id) {
        this.title = title;
        this.date = date;
        this.img_id = img_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
}

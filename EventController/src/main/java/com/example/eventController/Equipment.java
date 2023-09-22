package com.example.eventController;

public class Equipment {
    public int id_event;
    public int id;
    String title;
    int count_need;
    int count_exist;

    public Equipment(String title, int count_need, int count_exist) {
        this.title = title;
        this.count_need = count_need;
        this.count_exist = count_exist;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public void addOne()
    {
        count_exist++;
    }
    @Override
    public String toString() {
        DatabaseAdapter.getDBConnection();
        Event res = DatabaseAdapter.getEvent(id_event);
        if(null!=res)
            return res.title+": "+ title +"("+(count_need-count_exist)+")";
        else return title+"("+(count_need-count_exist)+")";

    }
}

package com.jerry.jtakeaway.responseBean;

import com.jerry.jtakeaway.bean.Jtransaction;
import com.jerry.jtakeaway.bean.User;

public class ResponseTransaction {
    private Jtransaction jtransaction;
    private User user;
    private User targetUser;

    public ResponseTransaction() {
    }

    public ResponseTransaction(Jtransaction jtransaction, User user, User targetUser) {
        this.jtransaction = jtransaction;
        this.user = user;
        this.targetUser = targetUser;
    }

    public Jtransaction getJtransaction() {
        return jtransaction;
    }

    public void setJtransaction(Jtransaction jtransaction) {
        this.jtransaction = jtransaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}

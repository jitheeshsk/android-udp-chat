package com.hiskysat.repository;


import androidx.room.Dao;

import com.hiskysat.entity.Chat;

import java.util.List;

@Dao
public interface ChatsDao {

    List<Chat> getChats(Long userId);

}

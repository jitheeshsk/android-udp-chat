package com.hiskysat.udpchat.mapper;

import android.text.TextUtils;

import com.hiskysat.data.ClientDto;
import com.hiskysat.udpchat.data.Client;

import java.util.Locale;

public class ClientMapper {

    public static Client clientFromClientDto(ClientDto clientDto) {
        return Client.builder()
                .clientId(clientDto.getId())
                .title(getClientTitle(clientDto))
                .build();
    }

    private static String getClientTitle(ClientDto clientDto) {
        String userName = clientDto.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return String.format(Locale.getDefault(), "%s:%s",
                    clientDto.getIpAddress(), clientDto.getPortNumber());
        } else {
            return String.format(Locale.getDefault(), "%s (%s:%s)",
                    userName, clientDto.getIpAddress(), clientDto.getPortNumber());
        }
    }

}

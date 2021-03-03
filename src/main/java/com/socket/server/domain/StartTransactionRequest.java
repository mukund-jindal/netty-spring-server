package com.socket.server.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class StartTransactionRequest implements Serializable {
    public String connectorId;
    public String idTag;
    public int meterStart;
    public int reservationId;
    public long timestamp;
}
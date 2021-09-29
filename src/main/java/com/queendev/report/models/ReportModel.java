package com.queendev.report.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor @Getter @Setter
public class ReportModel {

    private String playerName;
    private double amountReport;
    private Date date;
    private boolean visualized;
}

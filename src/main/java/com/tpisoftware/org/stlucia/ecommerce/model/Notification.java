package com.tpisoftware.org.stlucia.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 會員通知實體類別
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 通知標題

    @Column(nullable = false)
    private String content; // 通知內容

    @Column(nullable = false)
    private Date date; // 發送日期

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所屬會員

    private boolean isRead; // 已讀狀態（true 為已讀，false 為未讀）
}

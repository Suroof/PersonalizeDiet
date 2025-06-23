package com.itheima.zhinengti.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "user_preferences")
@Data
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 饮食类型
    @ElementCollection
    @CollectionTable(name = "user_diet_types")
    private Set<String> dietTypes;

    // 过敏原
    @ElementCollection
    @CollectionTable(name = "user_allergens")
    private Set<String> allergens;

    // 不喜欢的食材
    @ElementCollection
    @CollectionTable(name = "user_disliked_ingredients")
    private Set<String> dislikedIngredients;

    // 口味偏好
    @Column(name = "spiciness")
    private Integer spiciness = 2;

    @Column(name = "sweetness")
    private Integer sweetness = 2;

    @Column(name = "saltiness")
    private Integer saltiness = 2;

    // 烹饪时间偏好
    @Column(name = "max_cooking_time")
    private Integer maxCookingTime = 60;
}
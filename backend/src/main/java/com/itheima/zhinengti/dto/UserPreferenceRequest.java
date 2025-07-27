package com.itheima.zhinengti.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserPreferenceRequest {
    private Set<String> dietTypes;
    private Set<String> allergens;
    private Set<String> dislikedIngredients;
    private Integer spiciness;
    private Integer sweetness;
    private Integer saltiness;
    private Integer maxCookingTime;
}
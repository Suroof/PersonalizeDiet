package com.itheima.zhinengti.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {
    
    public static Long getCurrentUserId() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                Object userId = session.getAttribute("userId");
                if (userId != null) {
                    System.out.println("获取到用户ID: " + userId);
                    return (Long) userId;
                }
            }
            System.out.println("未找到用户ID，session为空或无userId属性");
            return null;
        } catch (Exception e) {
            System.out.println("获取用户ID时出错: " + e.getMessage());
            return null;
        }
    }
    
    public static String getCurrentUsername() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                return (String) session.getAttribute("username");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

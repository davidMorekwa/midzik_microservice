package com.midziklabs.advertisement.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurrentTenantHolder {
    private static final ThreadLocal<String> tenantHolder = new ThreadLocal<>();

    private String tenantId;

    public static String getCurrentTenant() {
        log.info("CurrentTenantHolder tenantId: " + tenantHolder.get());
        return tenantHolder.get();
    }
    public static void setCurrentTenant(String tenantId) {
        tenantHolder.set(tenantId);
    }
    public static void clear(){
        tenantHolder.remove();
    }

}

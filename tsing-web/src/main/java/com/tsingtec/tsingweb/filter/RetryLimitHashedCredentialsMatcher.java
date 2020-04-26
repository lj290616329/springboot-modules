package com.tsingtec.tsingweb.filter;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
	
	private Logger logger = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);
    // 密码重试缓存
    private Cache<String, AtomicInteger> passwordRetryCache;
    // 密码重试限制次数
    private Integer limitCount;
    
    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws ExcessiveAttemptsException{
    	
    	String username = (String) token.getPrincipal();
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if(retryCount.incrementAndGet() > limitCount) {
            logger.warn("贱人{}尝试登录超过{}次，账号已冻结", username, limitCount);
            throw new ExcessiveAttemptsException("尝试登录超过" + limitCount + "次，账号已冻结");
        }
        // 使用父类方法认证
        boolean matches = super.doCredentialsMatch(token, info);
        // 匹配通过，即登录成功，删除对应缓存
        if(matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }
    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }
    
    public void unlockAccount(String username){
    	passwordRetryCache.remove(username);
	}
}
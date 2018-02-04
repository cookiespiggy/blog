package com.yuantek.myblog.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 玩spring security,首先得有一个安全配置类
 * 
 * @author mi.zhe
 *
 */
/**
 * 网上竟瞎JB教,其实只需要定义成组件,spring就会感测得到,从而进行如下的配置
 * 默认是httpbase的模式,默认的用户名为user,密码启动的时候会看到
 * 如果这个类需要加密密码,就把这个类弄成配置类,造出密码加密的bean即可
 * 	配置类本身,也会成为spring的组件(bean)
 * @author mi.zhe
 *
 */

/**
 * 安全配置类
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级别的安全设置//AOP
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String KEY = "waylau.com";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // 使用BCrypt加密
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder); // 设置密码加密
		return authenticationProvider;
	}

	/**
	 * 自定义配置
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "fonts/**", "/index").permitAll() // 都可以访问
				.antMatchers("/h2-console/**").permitAll().antMatchers("/admins/**").hasRole("ADMIN").and().formLogin()
				.loginPage("/login").failureUrl("/login-error").and().rememberMe().key(KEY) // 启用remeber me
				.and().exceptionHandling().accessDeniedPage("/403"); // 处理异常,拒绝访问就是重定向到403界面

		http.csrf().ignoringAntMatchers("/h2-console/**");// 禁用H2控制台的CSRF防护
		http.headers().frameOptions().sameOrigin(); // 允许来自同一来源的H2控制台请求
	}

	/**
	 * 认证信息管理
	 * @param authenticationManagerBuilder
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService);
		authenticationManagerBuilder.authenticationProvider(authenticationProvider);
	}
}

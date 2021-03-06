package com.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.dao.Pwd_planDao;
import com.dao.Pwd_smtpDao;
import com.entity.Mail_Auth;
import com.entity.Pwd_plan;
import com.entity.Pwd_smtp;

public class MyTask extends TimerTask {
	public void run() {
		Calendar cal = Calendar.getInstance();
		long time = new Date().getTime();
		System.out.println("当前时间" + time);
		List selectAll = Pwd_planDao.SelectAll(String.valueOf(time));
		for (int i = 0; i < selectAll.size(); i++) {
			Pwd_plan Pwd_plan = (com.entity.Pwd_plan) selectAll.get(i);
			System.out.println(Pwd_plan.getContent());
		}
		if (selectAll.size() == 0) {
			System.out.println("当前无任务");
		} else {
			Pwd_smtp smtp = Pwd_smtpDao.Select();
			if (smtp.getHost() == null || smtp.getPort() == 0 || smtp.getUsername() == null
					|| smtp.getPassword() == null || smtp.getSub() == null) {
				System.out.println("邮件smtp未配置");
			} else {
				Mail_Auth mail = new Mail_Auth(smtp.getHost(), String.valueOf(smtp.getPort()), smtp.getUsername(),
						smtp.getPassword(), smtp.getSub());
				for (int i = 0; i < selectAll.size(); i++) {
					Pwd_plan plan = (com.entity.Pwd_plan) selectAll.get(i);
					int id = plan.getId();
					int userid = plan.getUserid();
					String email = plan.getEmail();
					String stime = plan.getStime();
					String content = plan.getContent();
					int type = plan.getType();
					String msgTitle = null;
					String msg = null;
					if (type == 1) {
						msgTitle = "系统信息";
						msg = "(预约)";
					} else if (type == 2) {
						msgTitle = "用户帐号激活";
						msg = "(注册)";
					} else if (type == 3) {
						msgTitle = "用户帐号重置";
						msg = "(重置)";
					}
					mail.setSender(smtp.getUsername());
					mail.setSubject(msgTitle);
					mail.setMessage(content);
					mail.setReceiver(email);
					if (plan.getStatus() == 0 && plan.getType() == 3
							|| Long.valueOf(stime) - Long.valueOf(time) <= 60 * 1000) {
						if (new MailUtil().send(mail)) {
							System.out.println("发送成功");
						} else {
							System.out.println("发送失败");
						}
						plan.setStatus(1);
						int j = Pwd_planDao.Update(plan);
					} else {
						System.out.println("当前任务不需要执行");
					}
				}
			}
		}
	}
}

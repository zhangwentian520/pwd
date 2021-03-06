package com.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.Pwd_smtpDao;
import com.entity.Pwd_smtp;
import com.entity.Pwd_user;
import com.struts.ActionForm;
import com.struts.ActionForward;
import com.struts.DispatchAction;
import com.util.GeneralUtil;

public class SmtpAction extends DispatchAction {
	public ActionForward set_smtp(HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		response.setContentType("text/html;charset=utf-8");
		Pwd_smtp smtp = (Pwd_smtp) form;
		Pwd_user user = (Pwd_user) request.getSession().getAttribute("user");
		if (user.getId() == 1) {
			int i = Pwd_smtpDao.Update(smtp);
			if (i > 0) {
				String str = GeneralUtil.EchoMsg("200", "请求网站SMTP修改成功", 0, null);
				try {
					response.getWriter().print(str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				String str = GeneralUtil.EchoMsg("201", "数据请求错误", 0, null);
				try {
					response.getWriter().print(str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				response.getWriter().print("<script language='javascript'>window.location.href='./404.jsp';</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

}

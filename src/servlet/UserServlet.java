package servlet;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import dao.UserDAO;
import util.Page;

public class UserServlet extends BaseBackServlet{
	UserDAO userDAO = new UserDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page){
		/*
		 * ���￼�ǵ�list��sqlд���� start<=id<=end ������mysql֧�ֵ�limit���:start��ʼ����count������
		 * ��ˣ��ڶ���������Ҫд���������� ������ֱ��д��page.count
		 * 
		 * ʵ�ʲ���������������Ҫʹ��limit�﷨ ��Ϊid�ǲ����ظ��ģ���ɾ����ĳ�����ݺ�id���ò���������ôûҳ
		 * ��ʾ����Ŀ�Ϳ��ܻ�����page.count�����磬����start=0 count=5 �������ݿ���ֻ�� 1 4��ô��һҳ��ֻ��ʾ����������
		 */
//		ArrayList<User> us = userDAO.list(page.getStart(),page.getStart()+page.getCount()-1);
		ArrayList<User> us = userDAO.list(page.getStart(),page.getCount());
		page.setTotal(userDAO.getTotal());
		
		request.setAttribute("us", us);
		request.setAttribute("page", page);
		
		return "admin/listUser.jsp";
	}
	
}

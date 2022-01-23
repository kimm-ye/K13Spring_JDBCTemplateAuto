package springboard.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springboard.model.JDBCTemplateDAO;
import springboard.model.SpringBbsDTO;

@Service
public class ViewCommand implements BbsCommandImpl{
	
	@Autowired
	JDBCTemplateDAO dao;
	
	@Override
	public void execute(Model model) {

		//요청 일괄 받기 (한꺼번에 받는거니까)
		Map<String, Object> paramMap = model.asMap();
		HttpServletRequest req = (HttpServletRequest)paramMap.get("req");
		SpringBbsDTO dto = (SpringBbsDTO)paramMap.get("springBbsDTO");
		
		dto = dao.view(String.valueOf(dto.getIdx()));

		//줄바꿈처리를 위해 <br/>로 변경
		dto.setContents(dto.getContents().replace("\r\n", "<br/>"));
		
		model.addAttribute("viewRow",dto);
		model.addAttribute("nowPage", dto.getNowPage());
		
	}
}

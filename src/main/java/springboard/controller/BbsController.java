package springboard.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springboard.command.BbsCommandImpl;
import springboard.command.DeleteActionCommand;
import springboard.command.EditActionCommand;
import springboard.command.EditCommand;
import springboard.command.ListCommand;
import springboard.command.ViewCommand;
import springboard.command.WriteActionCommand;
import springboard.model.JDBCTemplateDAO;
import springboard.model.SpringBbsDTO;

@Controller
public class BbsController {

	@Autowired
	private JDBCTemplateDAO dao;
	
	//모든 Command객체의 부모타입의 참조변수 생성
	BbsCommandImpl command = null;
	
	@Autowired
	ListCommand listCommand;
	@Autowired
	WriteActionCommand writeActionCommand;
	@Autowired
	ViewCommand viewCommand;
	@Autowired
	EditCommand editCommand;
	@Autowired
	DeleteActionCommand deleteActionCommand;
	@Autowired
	EditActionCommand editActionCommand;

	
	@RequestMapping("/board/list.do")
	public String list(Model model, HttpServletRequest req) {
		
		/*
		사용자로부터 받은 모든 요청은 request객체에 저장되고,
		이를 ListCommand 객체로 전달하기 위해 Model객체에 저장한 후 
		매개변수로 전달한다. 
		 */
		model.addAttribute("req",req); //request 객체 자체를 Model에 저장
		//command = new ListCommand(); //service 역할의 ListCommand객체 생성 //command가 부모타입(BbsCommandImpl)이기에 자식타입 참조할 수 있음
		command = listCommand;
		command.execute(model); //해당 객체로 Model객체 자체를 전달
		
		//return "07Board/list";
		return "BootSkin/listT";
	}
	
	//글쓰기 페이지로 진입하기 위한 매핑처리
	@RequestMapping("/board/write.do")
	public String write(Model model) {
		
		//return "07Board/write";
		return "BootSkin/writeT";
	}
	
	//전송방식이 post이므로 value, method 까지 같이 기술해서 매핑
	@RequestMapping(value="/board/writeAction.do", method = RequestMethod.POST)
	public String writeAction(Model model, HttpServletRequest req, SpringBbsDTO springBbsDTO) {
		/*
		글쓰기 페이지에서 전송된 모든 폼값은 SpringBbsDTO 객체를 통해 
		한번에 받을 수 있다. 
		Spring에서는 커맨드객체를 통해 이와 같은 처리를 할 수 있다.
		
		커맨드 객체를 이용하면 하나하나 넣지 않고 지금과 같이 하나의 객체를 통해 한번에 전송가능하다.
		 */
	
		//request객체와 함께 Model에 저장
		model.addAttribute("req",req);
		model.addAttribute("springBbsDTO",springBbsDTO);
		
		//요청을 전달할 service객체를 생성한 후 execute()메서드 호출
		//command = new WriteActionCommand();
		command = writeActionCommand;
		command.execute(model);
		
		//redirect를 통해 뷰를 반환하지 않고, 지정된 URL(요청명)로 이동한다. 
		return "redirect:list.do?nowPage=1";
	}
	
	//글 내용보기
	@RequestMapping("/board/view.do")
	public String view(Model model, HttpServletRequest req, SpringBbsDTO springBbsDTO) {
		
		//사용자의 요청을 저장한 request객체를 Model객체에 저장한 후 전달한다.
		//커맨드는 통으로 전달할 수 있으니까
		model.addAttribute("req",req);
		model.addAttribute("springBbsDTO", springBbsDTO);
		//command = new ViewCommand();
		command = viewCommand;
		command.execute(model);
		
		//return "07Board/view";
		return "BootSkin/viewT";
	}
	
	//패스워드 검증페이지 진입
	@RequestMapping("/board/password.do")
	public String password(Model model, HttpServletRequest req) {
		
		//일련번호는 컨트롤러에서 파라미터를 받은 후 Model에 저장하여 View로 전달한다.
		model.addAttribute("idx", req.getParameter("idx"));
		//return "07Board/password";
		return "BootSkin/passwordT";
	}
	//패스워드 검증
	@RequestMapping("/board/passwordAction.do")
	public String passwordAction(Model model, HttpServletRequest req) {
		
		String modePage = null;
		//폼값받기
		String mode = req.getParameter("mode");
		String idx = req.getParameter("idx");
		String nowPage = req.getParameter("nowPage");
		String pass = req.getParameter("pass");
		
		//DAO에서 일련번호와 패스워드를 통해 검증
		//JDBCTemplateDAO dao = new JDBCTemplateDAO();
		int rowExist = dao.password(idx, pass);
		
		if(rowExist <= 0) {
			//패스워드 검증 실패시에는 이전 페이지로 돌아간다.
			model.addAttribute("isCorrMsg", "패스워드가 일치하지 않습니다.");
			model.addAttribute("idx",idx);
			
			//패스워드 검증페이지를 반환한다.
			//modePage = "07Board/password";
			modePage = "BootSkin/passwordT";
		}
		
		else {
			//검증에 성공한 경우 수정 혹은 삭제 처리를 한다.
			System.out.println("검증완료");
			
			if(mode.equals("edit")) {
				
				/*
				 mode가 수정인 경우 수정페이지로 이동한다.
				 */
				model.addAttribute("req",req);
				//command = new EditCommand();
				command = editCommand;
				command.execute(model);
				
				//modePage = "07Board/edit";
				modePage = "BootSkin/editT";;
			}
			else if(mode.equals("delete")) {
				 //mode가 삭제인경우 즉시 삭제 처리
				
				model.addAttribute("req", req);
				//command = new DeleteActionCommand();
				command = deleteActionCommand;
				command.execute(model);
				
				
				//model에 저장하고 redirect하면 쿼리스트링 자동생성
				//삭제 후에는 리스트페이지로 이동한다.
				model.addAttribute("nowPage", req.getParameter("nowPage"));
				modePage = "redirect:list.do";
			}
		}
		
		return modePage;
	}
	
	//수정처리
	@RequestMapping("/board/editAction.do")
	public String editAction(HttpServletRequest req, Model model, SpringBbsDTO springBbsDTO) {
		
		/*
		request 내장객체와 수정페이지에서 전송한 모든 폼값을 저장한 DTO객체를
		Model에 저장한 후 서비스 객체로 전달한다.
		 */
		model.addAttribute("req",req);
		model.addAttribute("springBbsDTO",springBbsDTO);
		//command = new EditActionCommand();
		command = editActionCommand;
		command.execute(model);
		
		/*
		수정처리가 완료되면 상세페이지로 이동하게 되는데 이때 idx와 같은 파라미터가 필요하다.
		Model객체에 저장한 후 redirect하면 자동으로 쿼리스트링 형태로 만들어준다.
		
		즉, 별도로 redirect:view.do?idx=얼마&nowPage=얼마 쓸 필요없음.
		 */
		model.addAttribute("idx", req.getParameter("idx"));
		model.addAttribute("nowPage", req.getParameter("nowPage"));
		
		return "redirect:view.do";
	}
	
	
}







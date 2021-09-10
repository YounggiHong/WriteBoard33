package com.lec.spring.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.spring.WriteValidator;
import com.lec.spring.domain.WriteDTO;
import com.lec.spring.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	private BoardService boardService;

	@Autowired
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}
	
	public BoardController() {
		System.out.println("BoardController() 생성");
	}
	
	@RequestMapping("/list.do")
	public String list(Model model) {
		
		model.addAttribute("list", boardService.list());
		
		return "board/list";
	}
	
	@GetMapping("/write.do")
	public String write(Model model) {
		return "board/write";
	}	
	
	@PostMapping("/writeOk.do")
	public String writeOk(@ModelAttribute("w") @Valid WriteDTO dto
			, BindingResult result
			, Model model) {  // 핸들러 매개변수 작성시 Model 은 BindingResult 보다 뒤에 있어야 한다
		
		if(result.hasErrors()) {
			// 에러가능 추가적인 model attribute 지정 가능
			
			if(result.getFieldError("name") != null)
				model.addAttribute("ERR", result.getFieldError("name").getCode());
			else if(result.getFieldError("subject") != null)
				model.addAttribute("ERR", result.getFieldError("subject").getCode());
			
			return "board/write";
		}
		
		model.addAttribute("result", boardService.write(dto));
		model.addAttribute("dto", dto);   // auto-generated key
		
		return "board/writeOk";
	}
	
	
	@GetMapping("/view.do")
	public String view(int uid, Model model) {
		
		model.addAttribute("list", boardService.viewByUid(uid));
		
		return "board/view";
	}
	
	@RequestMapping("/update.do")
	public String update(int uid, Model model) {
		
		model.addAttribute("list", boardService.selectByUid(uid));
		
		return "board/update";
	}
	
	@PostMapping("/updateOk.do")
	public String updateOk(@ModelAttribute("w") @Valid WriteDTO dto
			, BindingResult result
			, RedirectAttributes redirectAttributes  // 이또한 BindingResult 이후에 명시
			, Model model) {
		
		if(result.hasErrors()) {
			// 에러가능 추가적인 model attribute 지정 가능
			if(result.getFieldError("subject") != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("SUBJECT", "제목입력은 필수입니다");
				redirectAttributes.addFlashAttribute("ERROR", map);				
				return "redirect:/board/update.do?uid=" + dto.getUid();
			}
		}
		
		model.addAttribute("result", boardService.update(dto));
		
		return "board/updateOk";  // forwarding
	}
	
	@GetMapping("/deleteOk.do")
	public String deleteOk(int uid, Model model) {
		
		model.addAttribute("result", boardService.deleteByUid(uid));
		
		return "board/deleteOk";		
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new WriteValidator());
	}
	
	
}


















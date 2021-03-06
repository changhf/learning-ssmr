package com.ssmr.chapter15.controller;

import com.ssmr.chapter15.pojo.Role;
import com.ssmr.chapter15.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/showRoleJsonInfo")
    public ModelAndView showRoleJsonInfo(Long id, String roleName, String note) {
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        mv.addObject("id", id);
        mv.addObject("roleName", roleName);
        mv.addObject("note", note);
        return mv;
    }

    /*
        Model为重定向数据模型，Spring MVC会自动初始化它
     */
    @RequestMapping("/addRole")
    public String addRole(Model model, String roleName, String note) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setNote(note);
        //插入角色后，会回填角色编号
        roleService.insertRole(role);
        //绑定重定向数据模型
        model.addAttribute("roleName", roleName);
        model.addAttribute("note", note);
        model.addAttribute("id", role.getId());
        return "redirect:./showRoleJsonInfo.do";
    }
    /*
       通过ModelAndView实现重定向
    */
    @RequestMapping("/addRole2")
    public ModelAndView addRole(ModelAndView mv, String roleName, String note) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setNote(note);
        //插入角色后，会回填角色编号
        roleService.insertRole(role);
        //绑定重定向数据模型
        mv.addObject("roleName", roleName);
        mv.addObject("note", note);
        mv.addObject("id", role.getId());
        mv.setViewName("redirect:./showRoleJsonInfo.do");
        return mv;
    }

    /*
        重定向-通过POJO传递对象
     */
    @RequestMapping("/showRoleJsonInfo2")
    public ModelAndView showRoleJsonInfo(Role role) {
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        mv.addObject("role", role);
        return mv;
    }

    /*
        重定向传递POJO
            RedirectAttributes
     */
    @RequestMapping("/addRole3")
    //RedirectAttribute对象Spring MVC会自动初始化它
    public String addRole3(RedirectAttributes ra, Role role) {
        //插入角色后，会回填角色编号
        roleService.insertRole(role);
        //绑定重定向数据模型
        ra.addFlashAttribute("role", role);
        System.out.println("控制器逻辑......");
        return "redirect:./showRoleJsonInfo2.do";
    }


    @RequestMapping(value = "/getRoleByModelMap", method = RequestMethod.GET)
    public ModelAndView getRoleByModelMap(@RequestParam("id") Long id, ModelMap modelMap) {
        Role role = roleService.getRole(id);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("roleDetails");
        modelMap.addAttribute("role", role);
        return mv;
    }

    @RequestMapping(value = "/getRoleByModel", method = RequestMethod.GET)
    public ModelAndView getRoleByModel(@RequestParam("id") Long id, Model model) {
        Role role = roleService.getRole(id);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("roleDetails");
        model.addAttribute("role", role);
        return mv;
    }

    @RequestMapping(value = "/getRoleByMv", method = RequestMethod.GET)
    public ModelAndView getRoleByMv(@RequestParam("id") Long id, ModelAndView mv) {
        Role role = roleService.getRole(id);
        mv.setViewName("roleDetails");
        mv.addObject("role", role);
        return mv;
    }

    @RequestMapping(value = "/getRoleForJson", method = RequestMethod.GET)
    public ModelAndView getRoleForJson(@RequestParam("id") Long id) {
        ModelAndView mv = new ModelAndView();
        Role role = roleService.getRole(id);
        mv.setView(new MappingJackson2JsonView());
        mv.addObject("role", role);
        return mv;
    }

    /*
        配置了InternalResourceViewResolver，所以能生成JstlView视图
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam("id") Long id, ModelMap model) {
        Role role = roleService.getRole(id);
        model.addAttribute("role", role);
        return "roleDetails";
    }
}

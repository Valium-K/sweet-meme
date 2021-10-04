package dev.valium.sweetmeme.module.notifications;

import dev.valium.sweetmeme.module.member.Member;
import dev.valium.sweetmeme.module.member.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor implements HandlerInterceptor {

    private final NotificationRepository notificationRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (isAuthorizedModelAndView(modelAndView, authentication)) {
            Member member = ((MemberUser)authentication.getPrincipal()).getMember();
            long count = notificationRepository.countByMemberIdAndHasRead(member.getId(), false);
            modelAndView.addObject("hasNotification", count);
        }
    }

    private boolean isAuthorizedModelAndView(ModelAndView modelAndView, Authentication authentication) {
        return modelAndView != null
                && !isRedirectView(modelAndView)
                && authentication != null
                && authentication.getPrincipal() instanceof MemberUser;
    }
    private boolean isRedirectView(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("redirect:")
                || modelAndView.getView() instanceof RedirectView;
    }
}
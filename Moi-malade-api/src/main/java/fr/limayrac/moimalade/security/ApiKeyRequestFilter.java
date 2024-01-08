package fr.limayrac.moimalade.security;

import fr.limayrac.moimalade.security.model.ApiKey;
import fr.limayrac.moimalade.security.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class ApiKeyRequestFilter extends GenericFilterBean {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        if (!path.startsWith("/api")) {
            chain.doFilter(request, response);
            return;
        }

        String key = req.getHeader("X-API-KEY") == null ? "" : req.getHeader("X-API-KEY");
        Optional<ApiKey> apiKey = apiKeyService.getApiKey(key);

        if (apiKey.isPresent()) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse resp = (HttpServletResponse) response;
            String error = "Cle API invalide";

            resp.reset();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentLength(error.length());
            response.getWriter().write(error);
        }
    }
}

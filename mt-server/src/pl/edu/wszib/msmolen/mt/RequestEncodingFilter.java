package pl.edu.wszib.msmolen.mt;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filtr ustawiajacy kodowanie znakow.
 */
@WebFilter("/*")
public class RequestEncodingFilter implements Filter
{

	public static final String ENCODING = "UTF-8";

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		request.setCharacterEncoding(ENCODING);

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException
	{
	}

}

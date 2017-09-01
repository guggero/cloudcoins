package ch.cloudcoins;

import ch.cloudcoins.environment.control.EnvironmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedMap;

import static ch.cloudcoins.environment.EnvironmentVariables.CORS_AWARE_FILTER;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CORSAwareFilterTest {

    @InjectMocks
    private CORSAwareFilter filter;

    @Mock
    private EnvironmentService environmentService;

    @Test
    public void shouldAddCorsAwareHeaderAttributes() throws Exception {
        // given
        doReturn("true").when(environmentService).getValue(CORS_AWARE_FILTER);
        MultivaluedMap<String, Object> headers = mock(MultivaluedMap.class);
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
        when(responseContext.getHeaders()).thenReturn(headers);

        // when
        filter.filter(null, responseContext);

        // then
        verify(headers, times(1)).add(eq("Access-Control-Allow-Origin"), anyString());
        verify(headers, times(1)).add(eq("Access-Control-Allow-Methods"), anyString());
        verify(headers, times(1)).add(eq("Access-Control-Allow-Headers"), anyString());
        verify(headers, times(1)).add(eq("Access-Control-Allow-Credentials"), anyString());
    }

    @Test
    public void shouldNotAddCorsAwareHeaderAttributes() throws Exception {
        // given
        doReturn("false").when(environmentService).getValue(CORS_AWARE_FILTER);
        MultivaluedMap<String, Object> headers = mock(MultivaluedMap.class);
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
        when(responseContext.getHeaders()).thenReturn(headers);

        // when
        filter.filter(null, responseContext);

        // then
        verify(headers, never()).add(eq("Access-Control-Allow-Origin"), anyString());
        verify(headers, never()).add(eq("Access-Control-Allow-Methods"), anyString());
        verify(headers, never()).add(eq("Access-Control-Allow-Headers"), anyString());
        verify(headers, never()).add(eq("Access-Control-Allow-Credentials"), anyString());
    }
}
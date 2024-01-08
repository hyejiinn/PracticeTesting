package sample.cafekiosk.spring.api.controller.order;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.product.ProductService;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest
{
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean // ProductService Mock 객체를 만들어서 컨테이너에 넣어준다.
	private OrderService orderService;
	
	@DisplayName("신규 주문을 등록한다.")
	@Test
	void createOrder() throws Exception
	{
		OrderCreateRequest request = OrderCreateRequest.builder()
				.productNumbers(List.of("001"))
				.build();
		
		mockMvc.perform(
						post("/api/v1/orders/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.code").value("200"))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.status").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")
								   .value("OK"));
		
	}
	
	@DisplayName("신규 주문을 등록할 때 상품번호는 1개 이상이어야 한다.")
	@Test
	void createOrderEmptyProductNumbers() throws Exception
	{
		OrderCreateRequest request = OrderCreateRequest.builder()
				.productNumbers(List.of())
				.build();
		
		mockMvc.perform(
						post("/api/v1/orders/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 번호 리스트는 필수입니다."))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
		
	}
	
	
}
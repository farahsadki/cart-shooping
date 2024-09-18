package com.example.panier_achats.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.panier_achats.model.Cart;
import com.example.panier_achats.model.OrderAddress;
import com.example.panier_achats.model.OrderRequest;
import com.example.panier_achats.model.ProductOrder;
import com.example.panier_achats.repository.CartRepository;
import com.example.panier_achats.repository.ProductOrderRepository;
import com.example.panier_achats.service.OrderService;
import com.example.panier_achats.util.CommonUtil;
import com.example.panier_achats.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Override
	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

		List<Cart> carts = cartRepository.findByUserId(userid);
		//throws : Utilisé dans une méthode pour indiquer qu'elle peut lancer des erreurs (exceptions) que l'appelant doit gérer.

//Exception : Type d'erreur que la méthode peut lancer. Cela signifie qu'il peut y avoir un problème que vous devez traiter.

		for (Cart cart : carts) {
 
			/*// Création d'une nouvelle commande en utilisant le constructeur avec paramètres*/
/*creer un product avec constructeur avec argumnts : ProductOrder order = new ProductOrder(
    UUID.randomUUID().toString(),           // orderId
    LocalDate.now(),                        // orderDate
    cart.getProduct(),                      // product
    cart.getProduct().getDiscountPrice(),   // price
    cart.getQuantity(),                     // quantity
    cart.getUser(),                         // user
    OrderStatus.IN_PROGRESS.getName(),      // status
    orderRequest.getPaymentType()           // paymentType
);
*/

			ProductOrder order = new ProductOrder();  
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct())_;
			order.setPrice(cart.getProduct().getDiscountPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			ProductOrder saveOrder = orderRepository.save(order);
			commonUtil.sendMailForProductOrder(saveOrder, "success");
		}
	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders = orderRepository.findByUserId(userId);
		return orders;
	}

@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById = orderRepository.findById(id);
		if (findById.isPresent()) {
			ProductOrder productOrder = findById.get();
			productOrder.setStatus(status);
			ProductOrder updateOrder = orderRepository.save(productOrder);
			return updateOrder;
		}
		return null;
	}
	
	
	
	

	@Override
	public List<ProductOrder> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return orderRepository.findAll(pageable);

	}

	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return orderRepository.findByOrderId(orderId);
	}

}

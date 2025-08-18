package com.mall.config;

import com.mall.entity.*;
import com.mall.repository.CategoryRepository;
import com.mall.repository.ProductRepository;
import com.mall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据初始化器 - 创建演示数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据
        if (userRepository.count() > 0) {
            return;
        }

        // 创建管理员用户
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@mall.com");
        admin.setPhone("13800138000");
        admin.setNickname("系统管理员");
        admin.setRole(User.UserRole.ADMIN);
        admin.setStatus(User.UserStatus.ACTIVE);
        admin.setMemberLevel(User.MemberLevel.DIAMOND);
        admin.setGrowthValue(10000);
        admin.setPoints(1000);
        userRepository.save(admin);

        // 创建测试用户
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("123456"));
        testUser.setEmail("test@mall.com");
        testUser.setPhone("13800138001");
        testUser.setNickname("测试用户");
        testUser.setRole(User.UserRole.USER);
        testUser.setStatus(User.UserStatus.ACTIVE);
        testUser.setMemberLevel(User.MemberLevel.GOLD);
        testUser.setGrowthValue(2000);
        testUser.setPoints(200);
        userRepository.save(testUser);

        // 创建商品分类
        Category electronics = new Category();
        electronics.setName("数码电子");
        electronics.setDescription("手机、电脑、数码配件等");
        electronics.setIcon("icon-electronics");
        electronics.setSortOrder(1);
        electronics.setStatus(Category.CategoryStatus.ACTIVE);
        categoryRepository.save(electronics);

        Category clothing = new Category();
        clothing.setName("服装鞋帽");
        clothing.setDescription("男装、女装、鞋子、配饰等");
        clothing.setIcon("icon-clothing");
        clothing.setSortOrder(2);
        clothing.setStatus(Category.CategoryStatus.ACTIVE);
        categoryRepository.save(clothing);

        Category home = new Category();
        home.setName("家居生活");
        home.setDescription("家具、家电、日用品等");
        home.setIcon("icon-home");
        home.setSortOrder(3);
        home.setStatus(Category.CategoryStatus.ACTIVE);
        categoryRepository.save(home);

        // 创建演示商品
        createProduct("iPhone 15 Pro", "苹果最新旗舰手机，A17 Pro芯片，钛金属设计", 
                     new BigDecimal("8999.00"), 100, electronics, true, true, false);
        
        createProduct("MacBook Air M3", "轻薄便携笔记本电脑，M3芯片强劲性能", 
                     new BigDecimal("9499.00"), 50, electronics, false, true, true);
        
        createProduct("AirPods Pro 3", "主动降噪无线耳机，空间音频体验", 
                     new BigDecimal("1899.00"), 200, electronics, true, false, true);

        createProduct("春季连衣裙", "优雅气质连衣裙，多色可选", 
                     new BigDecimal("299.00"), 150, clothing, false, true, false);
        
        createProduct("运动休闲鞋", "舒适透气运动鞋，适合日常穿着", 
                     new BigDecimal("399.00"), 80, clothing, true, false, true);

        createProduct("智能扫地机器人", "全自动清扫，智能规划路径", 
                     new BigDecimal("1299.00"), 30, home, false, true, true);
        
        createProduct("北欧风台灯", "简约设计台灯，护眼LED光源", 
                     new BigDecimal("199.00"), 60, home, true, false, false);

        System.out.println("=================================");
        System.out.println("演示数据初始化完成！");
        System.out.println("管理员账号: admin / admin123");
        System.out.println("测试用户: testuser / 123456");
        System.out.println("=================================");
    }

    private void createProduct(String name, String description, BigDecimal price, 
                              Integer stock, Category category, boolean isHot, 
                              boolean isNew, boolean isRecommend) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setOriginalPrice(price.multiply(new BigDecimal("1.2")));
        product.setStock(stock);
        product.setCategory(category);
        product.setStatus(Product.ProductStatus.ON_SALE);
        product.setIsHot(isHot);
        product.setIsNew(isNew);
        product.setIsRecommend(isRecommend);
        product.setSales(0);
        product.setSortOrder(1);
        product.setKeywords(name + "," + category.getName());
        
        // 设置默认图片URL
        product.setMainImage("https://via.placeholder.com/400x400?text=" + name.replaceAll(" ", "+"));
        product.setSubImages("https://via.placeholder.com/400x400?text=" + name.replaceAll(" ", "+") + "+1," +
                            "https://via.placeholder.com/400x400?text=" + name.replaceAll(" ", "+") + "+2");
        
        productRepository.save(product);
    }
}
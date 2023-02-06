package com.increff.pos.service;

public class BrandServiceTest {

//    @Autowired
//    private BrandService brandService;
//
//    @Before
//    public void generateData() throws ApiException {
//        createPojo("name1", "category1");
//        createPojo("name2", "category2");
//        createPojo("name3", "category3");
//        createPojo("name4", "category4");
//        createPojo("name5", "category5");
//    }
//
//    private BrandPojo createPojo(String name, String category) throws ApiException {
//        BrandPojo pojo = new BrandPojo();
//        pojo.setName(name);
//        pojo.setCategory(category);
////        brandService.create(pojo);
//        return pojo;
//    }
//
//    @Test
//    public void createTest() throws ApiException {
//        BrandPojo pojo = createPojo("name6", "category6");
//        assertNotNull(pojo);
//        assertNotNull(pojo.getId());
//    }
//
//    @Test(expected = ApiException.class)
//    public void createDuplicateTest() throws ApiException {
//        createPojo("name1", "category1");
//    }
//
//    @Test
//    public void getAll() {
//        List<BrandPojo> list = brandService.getAll();
//        assertEquals(5, list.size());
//    }
//
//    @Test
//    public void getById() {
//        List<BrandPojo> list = brandService.getAll();
//        assertEquals(5, list.size());
//        for (BrandPojo pojo : list) {
//            assertNotNull(brandService.getById(pojo.getId()));
//        }
//        Long wrongId = list.get(4).getId() + 1;
//        assertNull(brandService.getById(wrongId));
//    }
//
//    @Test
//    public void getByName() {
//        BrandPojo pojo = brandService.getOneByParameter("name", "name1");
//        assertNotNull(pojo.getName());
//        BrandPojo nullPojo = brandService.getOneByParameter("name", "name11");
//        assertNull(nullPojo);
//    }
//
//    @Test
//    public void getByCategory() {
//        BrandPojo pojo = brandService.getOneByParameter("category", "category1");
//        assertNotNull(pojo.getName());
//        BrandPojo nullPojo = brandService.getOneByParameter("category", "category11");
//        assertNull(nullPojo);
//    }
//
//    @Test
//    public void update() throws ApiException {
//        assertNull(brandService.getOneByParameter("name", "name11"));
//        BrandPojo pojo = brandService.getOneByParameter("name", "name1");
//        pojo.setName("name11");
//        pojo.setCategory("category11");
//        brandService.update(pojo.getId(), pojo);
//        assertNotNull(brandService.getOneByParameter("name", "name11"));
//    }
//
//    @Test(expected = ApiException.class)
//    public void updateDuplicate() throws ApiException {
//        BrandPojo pojo = brandService.getOneByParameter("name", "name1");
//        BrandPojo newPojo = new BrandPojo();
//        newPojo.setName("name4");
//        newPojo.setCategory("category4");
//        brandService.update(pojo.getId(), newPojo);
//    }
//
//    @Test(expected = ApiException.class)
//    public void updateWrongId() throws ApiException {
//        BrandPojo pojo = brandService.getOneByParameter("name", "name1");
//        BrandPojo newPojo = new BrandPojo();
//        newPojo.setName("name4");
//        newPojo.setCategory("category4");
//        brandService.update(1000L, newPojo);
//    }
//
//    @Test
//    public void getByBrandAndCategory() {
//        assertNotNull(brandService.getByNameAndCategory("name1", "category1"));
//        assertNull(brandService.getByNameAndCategory("name11", "category11"));
//    }


}



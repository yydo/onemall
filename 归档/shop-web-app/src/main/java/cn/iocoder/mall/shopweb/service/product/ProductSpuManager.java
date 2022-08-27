package cn.iocoder.mall.shopweb.service.product;

import cn.iocoder.common.framework.util.CollectionUtils;
import cn.iocoder.common.framework.vo.CommonResult;
import cn.iocoder.common.framework.vo.PageResult;
import cn.iocoder.mall.productservice.enums.spu.ProductSpuDetailFieldEnum;
import cn.iocoder.mall.productservice.rpc.category.ProductCategoryFeign;
import cn.iocoder.mall.productservice.rpc.category.dto.ProductCategoryRespDTO;
import cn.iocoder.mall.productservice.rpc.spu.ProductSpuFeign;
import cn.iocoder.mall.productservice.rpc.spu.dto.ProductSpuDetailRespDTO;
import cn.iocoder.mall.searchservice.enums.product.SearchProductConditionFieldEnum;
import cn.iocoder.mall.searchservice.rpc.product.SearchProductFeign;
import cn.iocoder.mall.searchservice.rpc.product.dto.SearchProductConditionReqDTO;
import cn.iocoder.mall.searchservice.rpc.product.dto.SearchProductConditionRespDTO;
import cn.iocoder.mall.searchservice.rpc.product.dto.SearchProductRespDTO;
import cn.iocoder.mall.shopweb.controller.product.vo.product.ProductSpuDetailRespVO;
import cn.iocoder.mall.shopweb.controller.product.vo.product.ProductSpuPageReqVO;
import cn.iocoder.mall.shopweb.controller.product.vo.product.ProductSpuRespVO;
import cn.iocoder.mall.shopweb.controller.product.vo.product.ProductSpuSearchConditionRespVO;
import cn.iocoder.mall.shopweb.convert.product.ProductSpuConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 商品 SPU Manager
 */
@Service
@Validated
public class ProductSpuManager {

    @Autowired
    private SearchProductFeign searchProductFeign;

    @Autowired
    private ProductCategoryFeign productCategoryFeign;
    @Autowired
    private ProductSpuFeign productSpuFeign;

    public PageResult<ProductSpuRespVO> pageProductSpu(ProductSpuPageReqVO pageReqVO) {
        CommonResult<PageResult<SearchProductRespDTO>> pageResult =
                searchProductFeign.pageSearchProduct(ProductSpuConvert.INSTANCE.convert(pageReqVO));
        pageResult.checkError();
        return ProductSpuConvert.INSTANCE.convertPage(pageResult.getData());
    }

    public ProductSpuSearchConditionRespVO getProductSpuSearchCondition(String keyword) {
        // 获得搜索条件
        CommonResult<SearchProductConditionRespDTO> getSearchProductConditionResult =
                searchProductFeign.getSearchProductCondition(new SearchProductConditionReqDTO().setKeyword(keyword)
                    .setFields(Collections.singletonList(SearchProductConditionFieldEnum.CATEGORY.getField())));
        getSearchProductConditionResult.checkError();
        // 拼接结果
        ProductSpuSearchConditionRespVO conditionRespVO = new ProductSpuSearchConditionRespVO();
        if (CollectionUtils.isEmpty(getSearchProductConditionResult.getData().getCids())) {
            conditionRespVO.setCategories(Collections.emptyList());
        } else {
            CommonResult<List<ProductCategoryRespDTO>> listProductCategoriesResult =
                    productCategoryFeign.listProductCategoriesByIds(getSearchProductConditionResult.getData().getCids());
            listProductCategoriesResult.checkError();
            conditionRespVO.setCategories(ProductSpuConvert.INSTANCE.convertList(listProductCategoriesResult.getData()));
        }
        return conditionRespVO;
    }

    public ProductSpuDetailRespVO getProductSpuDetail(Integer id) {
        CommonResult<ProductSpuDetailRespDTO> getProductSpuDetailResult = productSpuFeign.getProductSpuDetail(id,
                Arrays.asList(ProductSpuDetailFieldEnum.SKU.getField(), ProductSpuDetailFieldEnum.ATTR.getField()));
        getProductSpuDetailResult.checkError();
        return ProductSpuConvert.INSTANCE.convert(getProductSpuDetailResult.getData());
    }

}

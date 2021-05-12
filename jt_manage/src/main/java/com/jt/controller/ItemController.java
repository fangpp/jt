package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	/*//在bean对象实例化之后执行  一般都是为属性赋值用的
	@PostConstruct
	public void aa(){

	}

	//spring容器对象销毁之前 会执行关闭操作  一般用来释放链接/资源
	@PreDestroy
	public void bb(){

	}*/

	/**
	 * 业务说明: 根据分页实现商品查询
	 * URL地址:	http://localhost:8091/item/query?page=1&rows=20
	 * 参数:	page/rows
	 * 返回值:  EasyUITable
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page, Integer rows){

		return itemService.findItemByPage(page,rows);
	}

	/**
	 * 业务说明: 商品新增
	 * 	URL地址: 	http://localhost:8091/item/save
	 * 	请求参数: 	form表单 item对象接收
	 * 	返回值:		SysResult对象
	 *
	 * 重构新增:
	 * 		参数:  Item对象,ItemDesc对象
	 *
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item, ItemDesc itemDesc){

		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}


	/**
	 * 完成商品修改操作
	 * url地址:	http://localhost:8091/item/update
	 * 参数:    form表单
	 * 返回值:  SysResult对象
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc){

		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 批量删除商品
	 * url地址:	http://localhost:8091/item/delete
	 * 参数:    ids=100,101,102
	 * 返回值:  SysResult对象
	 * 简化操作: 当参数采用,号分隔时,会自动的转化为数组.
	 * SpringMVC 底层实现servlet
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long... ids){

		itemService.deleteItems(ids);
		return SysResult.success();
	}

	/**
	 * 实现上架/下架通用操作
	 * 	url: /item/reshelf   上架操作 status=1
	 * 	url: /item/instock   下架操作 status=2
	 * 	restFUl操作方法
	 * 	URL优化: url: /item/updateStatus/2
	 */
	@RequestMapping("/updateStatus/{status}")
	public SysResult updateStatus(@PathVariable Integer status,Long... ids){

		itemService.updateStatus(status,ids);
		return SysResult.success();
	}

	/**
	 * 根据id查询商品分类名称
	 * URL地址: /item/query/item/desc/'+data.id
	 * 参数:	id
	 * 返回值:  SysResult对象(itemDesc对象)
	 */
	@RequestMapping("/query/item/desc/{id}")
	public SysResult findItemDescById(@PathVariable Long id){

		ItemDesc itemDesc = itemService.findItemDescById(id);
		return SysResult.success(itemDesc);
	}




	/*@RequestMapping("/save")
	public SysResult saveItem(Item item){
		try {
			itemService.saveItem(item);
			return SysResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return SysResult.fail();
		}
	}*/
}

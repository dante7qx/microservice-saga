package org.dante.saga.springboot.storage.service.impl;

import org.apache.servicecomb.pack.omega.transaction.annotations.Compensable;
import org.dante.saga.springboot.storage.dao.StorageDAO;
import org.dante.saga.springboot.storage.dto.StorageDTO;
import org.dante.saga.springboot.storage.po.StoragePO;
import org.dante.saga.springboot.storage.service.IStorageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StorageServiceImpl implements IStorageService {

	@Autowired
	private StorageDAO storageDAO;

	@Override
	@Compensable(compensationMethod = "cancelDecreaseStock")
	@Transactional
	public void decreaseStock(String commodityCode, int count) {
		log.info("========> [ 库存扣减操作开始 ]");
		StoragePO storagePO = storageDAO.findByCommodityCode(commodityCode);
		if (storagePO != null && storagePO.getTotalCount() >= count) {
			storagePO.setTotalCount(storagePO.getTotalCount() - count);
			storageDAO.save(storagePO);
		} else {
			throw new RuntimeException("库存不足，无法出货！");
		}
	}

	/**
	 * 模拟扣减库存失败
	 * 
	 */
	@Override
	@Compensable(compensationMethod = "cancelDecreaseStock")
	@Transactional
	public void decreaseStockException(String commodityCode, int count) {
		log.info("扣减库存业务逻辑, 模拟异常，调用 decreaseStockException");
		StoragePO storagePO = storageDAO.findByCommodityCode(commodityCode);
		if (storagePO != null && storagePO.getTotalCount() >= count) {
			storagePO.setTotalCount(storagePO.getTotalCount() - count);
			storageDAO.save(storagePO);
		} else {
			throw new RuntimeException("库存不足，无法出货！");
		}
		throw new RuntimeException("扣减库存异常！");
	}

	/**
	 * 模拟扣减库存超时, 2秒超时
	 */
	@Override
	@Compensable(compensationMethod = "cancelDecreaseStock", timeout = 2, retries = 0, retryDelayInMilliseconds = 3000)
	@Transactional
	public void decreaseStockTimeout(String commodityCode, int count) {
		log.info("扣减库存，模拟超时，调用方法 decreaseStockTimeout");
		try {
			Thread.sleep(4000L);	// 模拟延迟 当前线程暂停4秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		StoragePO storagePO = storageDAO.findByCommodityCode(commodityCode);
		if (storagePO != null && storagePO.getTotalCount() >= count) {
			storagePO.setTotalCount(storagePO.getTotalCount() - count);
			storageDAO.save(storagePO);
		} else {
			throw new RuntimeException("库存不足，无法出货！");
		}
	}

	/**
	 * 扣减库存补偿操作
	 * 
	 * @param commodityCode
	 * @param count
	 */
	@Transactional
	public void cancelDecreaseStock(String commodityCode, int count) {
		log.info("[ 库存扣减补偿操作开始 ] <========");
		StoragePO storagePO = storageDAO.findByCommodityCode(commodityCode);
		if (storagePO != null) {
			storagePO.setTotalCount(storagePO.getTotalCount() + count);
			storageDAO.save(storagePO);
		} else {
			throw new RuntimeException(
					"库存扣减补偿操作失败, 可记录日志进行重试，Saga框架本身提供支持！[".concat(commodityCode).concat(",").concat(count + "]"));
		}
	}

	@Override
	public StorageDTO findByCommodityCode(String commodityCode) {
		StoragePO storagePO = storageDAO.findByCommodityCode(commodityCode);
		StorageDTO storageDTO = new StorageDTO();
		BeanUtils.copyProperties(storagePO, storageDTO);
		return storageDTO;
	}

}

package com.changgou.user.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.user.dao.AddressMapper;
import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:admin
 * @Description:Address业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class AddressServiceImpl extends CoreServiceImpl<Address> implements AddressService {

    private AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper) {
        super(addressMapper, Address.class);
        this.addressMapper = addressMapper;
    }

    @Override
    public List<Address> list(String userName) {
       /* Example address = new Example(Address.class);
        address.createCriteria().andEqualTo("username",userName);
        return addressMapper.selectByExample(address);
        select:根据实体中的属性值进行查询,查询条件使用等号;
        selectByExample:根据Example条件进行查询;
        Address address = new Address();
        address.setUsername(userName);
        return addressMapper.select(address);*/
        return addressMapper.list(userName);
    }
}

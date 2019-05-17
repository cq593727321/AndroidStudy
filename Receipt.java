package com.huawei.sde.iot.materials.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author March_CD
 * @since 2019-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DB_RECEIPT")
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收货记录单号
     */
    @TableField("CONSIGNEERECORD_ID")
    private String consigneerecordId;

    /**
     * 建账id
     */
    @TableField("CREATE_ACCOUNT_ID")
    private String createAccountId;

    /**
     * 建账状态，0未建账，50异常数据，100账务员驳回，200待账务员审批，201挂账人驳回，300待挂账人审批，400建账完成，500建账删除
     */
    @TableField("CREATE_ACCOUNT_STATUS")
    private Integer createAccountStatus;

    /**
     * 需求来源，1内部要货，2零星领料，3试制加工4，存量，5其他
     */
    @TableField("FROM_TYPE")
    private Integer fromType;

    /**
     * 来源单号
     */
    @TableField("FROM_NO")
    private String fromNo;

    /**
     * 挂账人姓名W3
     */
    @TableField("ACCOUNTCLERK_W3")
    private String accountclerkW3;

    /**
     * 挂账人所属部门编码
     */
    @TableField("DEPT_CODE")
    private String deptCode;

    /**
     * 挂账人部门名称(中)
     */
    @TableField("DEPT_NAME_CN")
    private String deptNameCn;

    /**
     * 挂账人部门名称(英)
     */
    @TableField("DEPT_NAME_EN")
    private String deptNameEn;

    /**
     * 挂账人lname
     */
    @TableField("ACCOUNTCLERK_LNAME")
    private String accountclerkLname;

    /**
     * 挂账人employee_number
     */
    @TableField("ACCOUNTCLERK_EMPNO")
    private String accountclerkEmpno;

    /**
     * 产线编码
     */
    @TableField("PROD_LINE_CODE")
    private String prodLineCode;

    /**
     * 产线英文名称
     */
    @TableField("PRODUCT_LINE_EN")
    private String productLineEn;

    /**
     * 产线中文名称
     */
    @TableField("PRODUCT_LINE_CN")
    private String productLineCn;

    /**
     * 公司条码
     */
    @TableField("COMPANY_CODE")
    private String companyCode;

    /**
     * 初始公司条码
     */
    @TableField("ORIGINAL_COMPANY_CODE")
    private String originalCompanyCode;

    /**
     * 研发条码
     */
    @TableField("DEVELOP_CODE")
    private String developCode;

    /**
     * 项目编码
     */
    @TableField("PROJECT_CODE")
    private String projectCode;

    /**
     * 项目名称(中)
     */
    @TableField("PROJECT_NAME_CN")
    private String projectNameCn;

    /**
     * 项目名称(英)
     */
    @TableField("PROJECT_NAME_EN")
    private String projectNameEn;

    /**
     * 物料编码
     */
    @TableField("MATERIAL_CODE")
    private String materialCode;

    /**
     * 物料版本
     */
    @TableField("MATERIAL_VERSION")
    private String materialVersion;

    /**
     * 物料单位
     */
    @TableField("MATERIAL_UNIT")
    private String materialUnit;

    /**
     * 物料描述(中文)
     */
    @TableField("MATERIAL_DESC_CN")
    private String materialDescCn;

    /**
     * 物料描述(英文)
     */
    @TableField("MATERIAL_DESC_EN")
    private String materialDescEn;

    /**
     * 物料发货数量
     */
    @TableField("MATERIAL_DELIVERGOODS_COUNT")
    private Integer materialDelivergoodsCount;

    /**
     * 物料确认数量
     */
    @TableField("MATERIAL_CONFIRMGOODS_COUNT")
    private Integer materialConfirmgoodsCount;

    /**
     * 发货时间
     */
    @TableField("SUPPLY_DELIVERGOODS_DATE")
    private LocalDateTime supplyDelivergoodsDate;

    /**
     * 物料地址
     */
    @TableField("MATERIAL_ADDRESS")
    private String materialAddress;

    /**
     * 收货备注
     */
    @TableField("CONSIGNEE_REMARK")
    private String consigneeRemark;

    /**
     * 收货人W3姓名
     */
    @TableField("CONSIGNEE_W3")
    private String consigneeW3;

    /**
     * 收货确认时间
     */
    @TableField("CONSIGNEE_CONFIRM_DATE")
    private LocalDateTime consigneeConfirmDate;

    /**
     * 供应链发货时间
     */
    @TableField("SUPPLY_LAST_UPDATE")
    private LocalDateTime supplyLastUpdate;

    /**
     * 楼宇主键
     */
    @TableField("ITEM_ID")
    private String itemId;

    /**
     * 房间主键
     */
    @TableField("ROOM_ID")
    private String roomId;

    /**
     * 装入物料编码
     */
    @TableField("MATERIAL_CODE_LOAD")
    private String materialCodeLoad;

    /**
     * 需求申请单号
     */
    @TableField("NEED_APPLICATION_NO")
    private String needApplicationNo;

    /**
     * 建账人id
     */
    @TableField("CREATE_ACCOUNTCLERK_W3")
    private String createAccountclerkW3;

    /**
     * 建账日期/转资产日期
     */
    @TableField("CREATE_ACCOUNT_DATE")
    private LocalDateTime createAccountDate;

    /**
     * 建账类型：1公司条码建账，2研发条码建账，3数量建账,4转费用化资产，5低值易耗品
     */
    @TableField("CREATE_ACCOUNT_TYPE")
    private Integer createAccountType;

    /**
     * 费用资产编号
     */
    @TableField("TURNASSETS_NO")
    private String turnassetsNo;

    /**
     * 所属关联码
     */
    @TableField("RELATED_CODE")
    private String relatedCode;

    /**
     * 物料类别(中文)
     */
    @TableField("MATERIAL_TYPE")
    private String materialType;

    /**
     * 物料类别(英文)
     */
    @TableField("MATERIAL_TYPE_NAME_EN")
    private String materialTypeNameEn;

    /**
     * 物料损耗属性：1消耗品，2非消耗品
     */
    @TableField("MATERIAL_ATTR")
    private Integer materialAttr;

    /**
     * 用途
     */
    @TableField("REQUIRE_USE")
    private String requireUse;

    /**
     * 需求的申请时间
     */
    @TableField("REQUIRE_APPLY_DATE")
    private LocalDateTime requireApplyDate;

    /**
     * 单板加工时间
     */
    @TableField("SUPPLY_PROCESS_DATE")
    private LocalDateTime supplyProcessDate;

    /**
     * 货架机柜号
     */
    @TableField("SHELF_NO")
    private String shelfNo;

    /**
     * 层号u号
     */
    @TableField("SHELF_LEVEL_NO")
    private String shelfLevelNo;

    /**
     * 槽位号
     */
    @TableField("SHELF_SLOT_NO")
    private String shelfSlotNo;

    /**
     * 质量状态，1故障，2良好
     */
    @TableField("QUALITY_STATUS")
    private Integer qualityStatus;

    /**
     * 物料状态，1使用中，5闲置中
     */
    @TableField("MATERIAL_STATUS")
    private Integer materialStatus;

    /**
     * 进入环境时间
     */
    @TableField("ENV_IN_DATE")
    private LocalDateTime envInDate;

    /**
     * 环境编码
     */
    @TableField("ENV_CODE")
    private String envCode;

    /**
     * 环境名称
     */
    @TableField("ENV_NAME")
    private String envName;

    /**
     * 是否来源于拆分，0:非拆分 1：拆分
     */
    @TableField("SPLIT_IS_FROM")
    private Integer splitIsFrom;

    /**
     * 拆分收货记录单号
     */
    @TableField("SPLIT_CONSIGNEERECORD_ID")
    private String splitConsigneerecordId;

    /**
     * 物料rfid
     */
    @TableField("MATERIAL_RFID")
    private String materialRfid;

    /**
     * 建账类型是否与建账基表匹配标识，0 建账类型与建账基表匹配，10建账类型与建账基表不匹配
     */
    @TableField("CREATE_ACCOUNT_CFG_FLAG")
    private Integer createAccountCfgFlag;

    /**
     * 公司条码与物料编码匹配标识，0匹配，10不匹配
     */
    @TableField("COMPANY_MATERIAL_CODE_FLAG")
    private Integer companyMaterialCodeFlag;

    /**
     * 挂账人维度，0受益人，1项目经理，受益人所在部门主管
     */
    @TableField("CREATE_ACCOUNTCLERK_TYPE")
    private Integer createAccountclerkType;

    /**
     * 转审用户id及时间串，以逗号分隔，格式：转出人id/yyyy-mm-dd hh:mm:ss/转入人id
     */
    @TableField("TRANSFER_AUDIT_IDS")
    private String transferAuditIds;

    /**
     * 是否到货标记，0否，1是
     */
    @TableField("RECEIVED_FLAG")
    private Integer receivedFlag;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private Integer createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATION_DATE")
    private LocalDateTime creationDate;

    /**
     * 最后修改人
     */
    @TableField("LAST_UPDATED_BY_W3")
    private String lastUpdatedByW3;

    /**
     * 最后修改时间
     */
    @TableField("LAST_UPDATED_DATE")
    private LocalDateTime lastUpdatedDate;

    /**
     * 数据集成来源，默认0，1imall试制加工
     */
    @TableField("INTEGRATION_TYPE")
    private Integer integrationType;

    /**
     * 集成来源id
     */
    @TableField("INTEGRATION_ID")
    private String integrationId;

    /**
     * 创建人
     */
    @TableField("CREATED_BY_W3")
    private String createdByW3;

    /**
     * 是否手动收货
     */
    @TableField("MANUAL_CONSIGNEE_FLAG")
    private Integer manualConsigneeFlag;

    /**
     * 是否删除
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;


}

<template>
  <el-card class="glass-card">
    <template #header>
      <div class="head">
        <span>报销填报管理</span>
        <el-button type="primary" @click="openDialog()">新增报销</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading">
      <el-table-column label="报销单号" prop="reimburseNo" width="180"/>
      <el-table-column label="目的地" prop="destination"/>
      <el-table-column label="总金额" prop="totalAmount" width="110"/>
      <el-table-column label="发票" width="150">
        <template #default="scope">
          <div v-if="invoiceList(scope.row.invoiceImages).length" class="invoice-cell">
            <el-image
              :src="firstInvoice(scope.row.invoiceImages)"
              :preview-src-list="invoiceList(scope.row.invoiceImages)"
              preview-teleported
              fit="cover"
              class="invoice-thumb"
            >
              <template #error>
                <div class="invoice-fallback">无图</div>
              </template>
            </el-image>
            <el-tag size="small" effect="plain">共 {{ invoiceList(scope.row.invoiceImages).length }} 张</el-tag>
          </div>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120"><template #default="scope"><el-tag :type="statusTag(scope.row.status)">{{ statusText(scope.row.status) }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
          <el-button link type="primary" @click="submit(scope.row)" :disabled="scope.row.status !== '0'">提交</el-button>
          <el-button link type="danger" @click="remove(scope.row)" :disabled="scope.row.status !== '0'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="open" width="760px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="目的地" prop="destination"><el-input v-model="form.destination"/></el-form-item>
        <el-form-item label="交通费"><el-input-number v-model="form.trafficAmount" :min="0" :precision="2" style="width:100%"/></el-form-item>
        <el-form-item label="住宿费"><el-input-number v-model="form.hotelAmount" :min="0" :precision="2" style="width:100%"/></el-form-item>
        <el-form-item label="餐饮费"><el-input-number v-model="form.mealAmount" :min="0" :precision="2" style="width:100%"/></el-form-item>
        <el-form-item label="其他费用"><el-input-number v-model="form.otherAmount" :min="0" :precision="2" style="width:100%"/></el-form-item>
        <el-form-item label="费用说明" prop="expenseDesc"><el-input v-model="form.expenseDesc" type="textarea" :rows="3"/></el-form-item>
        <el-form-item label="发票上传">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            list-type="picture-card"
            v-model:file-list="uploadFileList"
            :on-success="onUploadSuccess"
            :on-remove="onUploadRemove"
            :on-preview="onUploadPreview"
            :on-error="onUploadError"
            :before-upload="beforeUpload"
            :limit="6"
            accept="image/*"
            multiple
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="invoice-tip">支持 JPG/PNG，单张不超过 5MB，最多 6 张。</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
        <el-button @click="open=false">取消</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { computed, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getToken } from '@/utils/auth'
import { travelApi } from '@/api/travel'

const loading = ref(false)
const list = ref([])
const open = ref(false)
const dialogTitle = ref('')
const form = ref({})
const formRef = ref()
const imageUrls = ref([])
const uploadFileList = ref([])
const saving = ref(false)

const uploadUrl = '/dev-api/common/upload'
const uploadHeaders = computed(() => ({ Authorization: 'Bearer ' + getToken() }))
const rules = {
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  expenseDesc: [{ required: true, message: '请填写费用说明', trigger: 'blur' }]
}

function statusText(status) {
  return { '0': '草稿', '1': '待审批', '2': '待支付', '3': '已支付', '4': '已驳回' }[status] || status
}
function statusTag(status) {
  return { '0': 'info', '1': 'warning', '2': '', '3': 'success', '4': 'danger' }[status] || ''
}

function onUploadSuccess(res, file, files) {
  if (res.code === 200) {
    file.url = res.url
    uploadFileList.value = [...files]
    syncInvoiceImages()
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}

function onUploadError() {
  ElMessage.error('上传失败，请稍后重试')
}

function onUploadRemove(file, files) {
  uploadFileList.value = [...files]
  syncInvoiceImages()
}

function onUploadPreview(file) {
  if (file.url) {
    window.open(file.url, '_blank')
  }
}

function beforeUpload(rawFile) {
  const isImage = rawFile.type && rawFile.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('仅支持上传图片文件')
    return false
  }
  const isLt5M = rawFile.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('单张图片不能超过 5MB')
    return false
  }
  return true
}

function syncInvoiceImages() {
  imageUrls.value = uploadFileList.value
    .map(item => item.url || item.response?.url)
    .filter(url => !!url)
  form.value.invoiceImages = imageUrls.value.join(',')
}

function invoiceList(images) {
  if (!images) return []
  return images
    .split(',')
    .map(v => v.trim())
    .filter(v => !!v)
}

function firstInvoice(images) {
  const items = invoiceList(images)
  return items.length ? items[0] : ''
}

async function load() {
  loading.value = true
  const res = await travelApi.listMyReimburse({ pageNum: 1, pageSize: 100 })
  list.value = res.rows || []
  loading.value = false
}

function openDialog(row) {
  if (row) {
    dialogTitle.value = '编辑报销'
    form.value = { ...row }
    imageUrls.value = invoiceList(row.invoiceImages)
    uploadFileList.value = imageUrls.value.map((url, index) => ({ name: 'invoice-' + (index + 1), url }))
  } else {
    dialogTitle.value = '新增报销'
    form.value = { destination: '', trafficAmount: 0, hotelAmount: 0, mealAmount: 0, otherAmount: 0, expenseDesc: '', invoiceImages: '' }
    imageUrls.value = []
    uploadFileList.value = []
  }
  open.value = true
}

async function save() {
  await formRef.value.validate()
  const total = (form.value.trafficAmount || 0) + (form.value.hotelAmount || 0) + (form.value.mealAmount || 0) + (form.value.otherAmount || 0)
  if (total <= 0) {
    ElMessage.warning('请至少填写一项费用金额')
    return
  }
  saving.value = true
  try {
    form.value.invoiceImages = imageUrls.value.join(',')
    if (form.value.reimburseId) await travelApi.updateMyReimburse(form.value)
    else await travelApi.addMyReimburse(form.value)
    ElMessage.success('保存成功')
    open.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function submit(row) {
  if (!invoiceList(row.invoiceImages).length) {
    ElMessage.warning('提交前请至少上传一张发票')
    return
  }
  await travelApi.submitMyReimburse(row.reimburseId)
  ElMessage.success('提交成功')
  load()
}

async function remove(row) {
  await ElMessageBox.confirm('确认删除该报销单？', '提示', { type: 'warning' })
  await travelApi.delMyReimburse(row.reimburseId)
  ElMessage.success('删除成功')
  load()
}

load()
</script>

<style scoped>
.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.invoice-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.invoice-thumb {
  width: 46px;
  height: 46px;
  border-radius: 8px;
  flex-shrink: 0;
}

.invoice-fallback {
  width: 46px;
  height: 46px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  font-size: 12px;
}

.invoice-tip {
  margin-top: 8px;
  color: var(--text-sub);
  font-size: 12px;
}
</style>

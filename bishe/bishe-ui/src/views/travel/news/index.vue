<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true" v-show="showSearch" ref="queryRef">
      <el-form-item label="标题"><el-input v-model="queryParams.newsTitle" clearable @keyup.enter="getList"/></el-form-item>
      <el-form-item label="类型">
        <el-select v-model="queryParams.newsType" clearable style="width: 180px">
          <el-option v-for="d in biz_news_type" :key="d.value" :label="d.label" :value="d.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="getList">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" v-if="proxy.$auth.hasPermi('travel:news:add')">
      <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button></el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="list" style="width: 100%">
      <el-table-column label="封面" width="100">
        <template #default="scope"><image-preview :src="scope.row.coverImage" :width="50" :height="50"/></template>
      </el-table-column>
      <el-table-column label="标题" prop="newsTitle" min-width="220"/>
      <el-table-column label="类型" width="120"><template #default="scope"><dict-tag :options="biz_news_type" :value="scope.row.newsType"/></template></el-table-column>
      <el-table-column label="状态" width="120" align="center"><template #default="scope"><dict-tag :options="biz_news_status" :value="scope.row.publishStatus"/></template></el-table-column>
      <el-table-column label="发布时间" prop="publishTime" width="180"/>
      <el-table-column label="操作" width="220" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button link type="primary" v-hasPermi="['travel:news:edit']" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" v-hasPermi="['travel:news:remove']" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" v-model="open" width="760px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="标题" prop="newsTitle"><el-input v-model="form.newsTitle"/></el-form-item>
        <el-form-item label="类型" prop="newsType"><el-select v-model="form.newsType" style="width: 100%"><el-option v-for="d in biz_news_type" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
        <el-form-item label="状态" prop="publishStatus"><el-select v-model="form.publishStatus" style="width: 100%"><el-option v-for="d in biz_news_status" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
        <el-form-item label="封面图片"><image-upload v-model="form.coverImage" :limit="1" v-if="mode!=='view'"/><image-preview :src="form.coverImage" :width="100" :height="100" v-else/></el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.newsContent" :min-height="192" v-if="mode!=='view'"/>
          <div v-else v-html="form.newsContent" class="news-view-content"></div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button v-if="mode==='view'" @click="open=false">关闭</el-button>
        <template v-else>
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="open=false">取消</el-button>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelNews">
import { listPublishedNews, listNews, addNews, updateNews, delNews } from '@/api/travel/news'

const { proxy } = getCurrentInstance()
const { biz_news_type, biz_news_status } = proxy.useDict('biz_news_type', 'biz_news_status')

const loading = ref(false)
const showSearch = ref(true)
const open = ref(false)
const mode = ref('view')
const title = ref('')
const list = ref([])
const total = ref(0)
const formRef = ref(null)
const queryParams = ref({ pageNum: 1, pageSize: 10, newsTitle: undefined, newsType: undefined })
const form = ref({})
const rules = { newsTitle: [{ required: true, message: '请输入标题', trigger: 'blur' }] }

function getList() {
  loading.value = true
  const req = proxy.$auth.hasPermi('travel:news:list') ? listNews(queryParams.value) : listPublishedNews(queryParams.value)
  req.then(res => {
    list.value = res.rows
    total.value = res.total
    loading.value = false
  })
}
function resetQuery() {
  queryParams.value = { pageNum: 1, pageSize: 10, newsTitle: undefined, newsType: undefined }
  getList()
}
function handleAdd() {
  mode.value = 'edit'
  title.value = '新增资讯'
  form.value = { publishStatus: '0' }
  open.value = true
}
function handleEdit(row) {
  mode.value = 'edit'
  title.value = '编辑资讯'
  form.value = { ...row }
  open.value = true
}
function handleView(row) {
  mode.value = 'view'
  title.value = '资讯详情'
  form.value = { ...row }
  open.value = true
}
function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    const req = form.value.newsId ? updateNews(form.value) : addNews(form.value)
    req.then(() => {
      proxy.$modal.msgSuccess('操作成功')
      open.value = false
      getList()
    })
  })
}
function handleDelete(row) {
  proxy.$modal.confirm('确认删除该资讯吗？').then(() => delNews(row.newsId)).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  })
}

getList()
</script>

<style scoped>
.news-view-content {
  border: 1px solid #dcdfe6;
  padding: 15px;
  border-radius: 4px;
  min-height: 200px;
  max-height: 500px;
  overflow-y: auto;
}
.news-view-content :deep(img) {
  max-width: 100%;
}
</style>

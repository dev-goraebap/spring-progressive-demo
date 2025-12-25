@import xyz.goraebap.blog.infra.view_model.Pagination
@import xyz.goraebap.blog.infra.view_model.CurationViewModel

@param Pagination<CurationViewModel> curationData

@template.pages.curation.index.list(items = curationData.getItems())
@template.pages.curation.index.pagination(curationData = curationData, oob = true)

@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel

@param Pagination<CurationViewModel> curationData

@template.pages.curation._list(items = curationData.getItems())
@template.pages.curation._pagination(curationData = curationData, oob = true)

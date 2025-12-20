@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel

@param Pagination<PostViewModel> postData

@template.entities.post.list(items = postData.getItems())
@template.entities.post.pagination(postData = postData, oob = true)

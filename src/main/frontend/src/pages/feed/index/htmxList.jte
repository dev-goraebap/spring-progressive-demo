@import xyz.goraebap.blog.infra.view_model.Pagination
@import xyz.goraebap.blog.infra.view_model.PostViewModel

@param Pagination<PostViewModel> postData

@template.entities.post.list(items = postData.getItems())
@template.entities.post.pagination(postData = postData, oob = true)

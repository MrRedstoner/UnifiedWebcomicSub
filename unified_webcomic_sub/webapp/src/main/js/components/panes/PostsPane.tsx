'use strict';

import React from 'react';
import { UserPermissionClosure } from '../../api/entities';
import PostCreate from '../create/PostCreate';
import PostDetail from '../detail/PostDetail';
import PostList from '../list/PostList';
import { Route, Routes } from 'react-router-dom';

type Props = {
	user: UserPermissionClosure;
}

const PostsPane: React.FC<Props> = ({ user }) => {
	//TODO: create, view, list

	return (<><h2>Posts</h2>
		<Routes>
			<Route path="new" element={<PostCreate user={user} />} />
			<Route path="show">
				<Route path=":id" element={<PostDetail user={user} />} />
			</Route>
			<Route path="/" element={<PostList user={user} />} />
		</Routes>
	</>);
}

export default PostsPane;
'use strict';

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { UserPermissionClosure } from '../../api/entities';

type Props = {
	user: UserPermissionClosure;
}

const PostList: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const onNew = () => { navigate("/posts/new"); };
	return (<>
		<button onClick={onNew} disabled={!user.createPost}>New</button>
		<h2>PostList</h2></>
	);
}

export default PostList;
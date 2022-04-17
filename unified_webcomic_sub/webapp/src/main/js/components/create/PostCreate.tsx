'use strict';

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import InputBox from '../InputBox';
import { Post, UserPermissionClosure } from '../../api/entities'
import { asyncFetchPost } from '../../api/apiCall';
import { POST_SERVICE_CREATE } from '../../api/apiEndpoints'

type Props = {
	user: UserPermissionClosure;
}

const blankPost: Post = { title: "", content: "" }

const PostCreate: React.FC<Props> = ({ user }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(true);
	const [post, setPost] = useState<Post>(blankPost);
	const navigate = useNavigate();

	const onBack = () => { navigate("/posts"); };

	const canPost = user.createPost && post.content !== "" && post.title !== "";

	const onTitle = (title: string) => {
		const newPost = Object.assign({}, post);
		newPost.title = title;
		setPost(newPost);
	}
	const onContent = (content: string) => {
		const newPost = Object.assign({}, post);
		newPost.content = content;
		setPost(newPost);
	}

	const onCreated = (id: number) => {
		navigate("/posts/show/" + id);
	}

	const onPost = async () => {
		setIsLoaded(false);
		setError(null);
		await asyncFetchPost(POST_SERVICE_CREATE, post, onCreated, setError, setIsLoaded);
	};

	const form = (<>
		<button onClick={onBack}>Back to list</button>
		<br />
		<InputBox label="Title" initialValue="" setValue={onTitle}></InputBox>
		<br />
		<InputBox label="Content" initialValue="" setValue={onContent}></InputBox>
		<br />
	</>);

	if (error) {
		return (
			<div>
				{form}
				<div>Error: {error}</div>
				<button onClick={onPost} disabled={!canPost}>Post</button>
			</div>);
	} else if (!isLoaded) {
		return (
			<div>
				{form}
				<button disabled={true}>Loading...</button>
			</div>);
	} else {
		return (
			<div>
				{form}
				<button onClick={onPost} disabled={!canPost}>Post</button>
			</div>);
	}
}

export default PostCreate;
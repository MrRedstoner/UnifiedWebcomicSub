'use strict';

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import InputBox from '../InputBox';
import { Post, UserPermissionClosure } from '../../api/entities'
import { asyncFetchPost } from '../../api/apiCall';
import { POST_SERVICE_CREATE } from '../../api/apiEndpoints'
import { compact, containsAny } from '../../util/Utils';

type OLProps = {
	options: string[];
	addOption: (option: string) => void;
	delOption: (i: number) => void;
}
const OptionsList: React.FC<OLProps> = ({ options, addOption, delOption }) => {
	const [opt, setOpt] = useState<string>("");

	const lines = containsAny(options) ?
		<br /> :
		options.map((opt, i) =>
			<div key={i}>
				{opt}
				<button onClick={() => delOption(i)}>X</button>
			</div>);
	return <>
		{lines}
		<InputBox label="Poll option" initialValue="" setValue={setOpt} />
		<button disabled={opt === ""} onClick={() => { addOption(opt) }}>Add Option</button>
	</>
}

type Props = {
	user: UserPermissionClosure;
}

const blankPost: Post = { title: "", content: "" }

const PostCreate: React.FC<Props> = ({ user }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(true);
	const [post, setPost] = useState<Post>(blankPost);
	const [options, setOptions] = useState<string[]>([]);
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

	const addOption = (content: string) => {
		const newOptions = options.slice();
		newOptions.push(content);
		setOptions(newOptions);
	}

	const removeOption = (i: number) => {
		const newOptions = options.slice();
		delete newOptions[i];
		setOptions(newOptions);
	}
	const onCreated = (id: number) => {
		navigate("/posts/show/" + id);
	}

	const onPost = async () => {
		setIsLoaded(false);
		setError(null);
		const data: any = Object.assign({}, post);
		data.options = compact(options);
		await asyncFetchPost(POST_SERVICE_CREATE, data, onCreated, setError, setIsLoaded);
	};

	//TODO text area
	const form = (<>
		<button onClick={onBack}>Back to list</button>
		<br />
		<InputBox label="Title" initialValue="" setValue={onTitle}></InputBox>
		<br />
		<InputBox label="Content" initialValue="" setValue={onContent}></InputBox>
		<OptionsList options={options} addOption={addOption} delOption={removeOption} />
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
<template>
    <h2 class="card-title"><i><strong>File Show</strong></i>
        <!--"上传" --------"上传"-----------"上传"-------------"上传"---------"上传" -------------------->
        <!--  <input type="button" value="上传"  class="shangchuan"> -->
        <div >
            <input type="button" value="下载" class="xiazai">
            <span class="dropdown xinjian" style="margin-left: 20px">
						  <span>新建选项</span>
						  <span class="dropdown-content ">
						    <input type="button" value="新建文件夹" class="xinjian" onclick="createFolder()" />
						    <input type="button" value="新建文件" class="xinjian" onclick="dianji1()" />
						  </span>
						</span>
            <a href="javascript:void(0);" class="return" onclick="returnFloder(this)" data-xpath="{{xpath}}">返回上一级</a>&nbsp&nbsp&nbsp&nbsp
            <a href="javascript:void(0);" class="reflash" onclick="reflash(this)" data-xpath="{{xpath}}">刷新</a>
        </div>
        <span class="btn btn-success fileinput-button" >
					        <i class="glyphicon glyphicon-plus"></i>
					        <span>Add files...</span>
            <!-- The file input field used as target for the file upload widget -->
						    <input id="fileupload" type="file" name="files[]" onchange="test()"  multiple>
    					</span>
        <form id="upload" name="upload"  method="post" enctype="multipart/form-data" >
            <!-- <input type="file" name="filename" id="file" value="上传" class="ui-input-file" multiple >
            <input type="button" value="上传"  class="shangchuan"> -->
        </form>
        <!--   <div id="lujing">
              <input id="lujing" type="text" value="{{xpath}}" readonly="readonly" disabled="true" />

          </div><br/> -->


        <br/>

    </h2>
    <span >
		            <a  href ="javascript:void(0)" data-path="/" onclick="goTo(this)">全部文件 /</a>
		               <a  v-for="path in paths" href ="javascript:void(0)" data-path="{{path}}" onclick="goTo(this)">{{path}} /</a>
		              </span>
    <div class="table-responsive">
        <table class="table table-striped" >
            <thead>
            <tr>
                <td><input type="checkbox" name =selectAll> </td>
                <th>icon</th>
                <th >folder name</th>
                <th>modification_time</th>
                <th>owner</th>
                <th>blocksize</th>
                <th>block_replication</th>
                <th>operation</th>
            </tr>
            </thead>
            <tbody>
            <tr v-show="newFileShow==1">
                <td><input type="checkbox" name => </td>
                <td><img src="${rc.contextPath}/images/file.png" alt="folder"></td>
                <td><input type="text"  id="createFile" value="新建文件" >
                    <img src="${rc.contextPath}/images/dui.png" class="duicuo" onclick="createFile(this)" data-xpath="{{xpath}}" ><img src="${rc.contextPath}/images/cuo.png" class="duicuo" onclick="cancel()"></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr v-show="newFileShow==2">
                <td><input type="checkbox" name => </td>
                <td><img src="${rc.contextPath}/images/folder.png" alt="folder"></td>
                <td><input type="text"  id="xfloderpath" value="新建文件夹">
                    <img src="${rc.contextPath}/images/dui.png" class="duicuo" onclick="createDir(this)" data-xpath="{{xpath}}"><img src="${rc.contextPath}/images/cuo.png" class="duicuo" onclick="cancel()">
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr v-for="file in list" name={{file.absolutePath}} data-url="{{file.absolutePath}}">
                <td><input type="checkbox" name ={{file.absolutPath}}> </td>
                <td v-if="file.suffix === 'dir'" onclick="selectFile(this)" data-dir="{{file.isdir}}" data-url="{{file.absolutePath}}"><img src="${rc.contextPath}/images/folder.png" alt="folder"></td>
                <td v-if="file.suffix === 'jpg'||file.suffix === 'png'||file.suffix === 'gif'" onclick="previewImg(this)" data-dir="{{file.isdir}}" data-url="{{file.absolutePath}}" ><img src="${rc.contextPath}/images/image.png" alt="folder"></td>
                <td v-if="file.suffix === 'txt'" onclick="selectFile(this)" data-dir="{{file.isdir}}" data-url="{{file.absolutePath}}"><img src="${rc.contextPath}/images/file.png" alt="folder"></td>
                <td v-if="file.suffix === 'avi'||file.suffix === 'mp4'||file.suffix === 'rmvb'" onclick="previewMov(this)" data-dir="{{file.isdir}}" data-url="{{file.absolutePath}}"><img src="${rc.contextPath}/images/mov.png" alt="folder"></td>
                <td v-if="file.suffix === 'none'" onclick="selectFile(this)" data-dir="{{file.isdir}}" data-url="{{file.absolutePath}}"><img src="${rc.contextPath}/images/file.png" alt="folder"></td>
                <td v-if="!file.rename" onclick="selectFile(this)" data-dir="{{file.isdir}}" data-url="{{file.absolutePath}}">{{file.name}}</td>
                <td v-if="file.rename"><input type="text" id={{file.name}} value={{file.name}} onblur="re()" ></td>
                <td>{{file.modification_time}}</td>
                <td>{{file.owner}}</td>
                <td>{{file.blocksize}}</td>
                <td>{{file.block_replication}}</td>
                <template v-if="file.suffix === 'dir'">
                    <th><a href="#" onclick="rename(this)" data-url="{{file.absolutePath}}">重命名</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="del(this)" data-url="{{file.absolutePath}}">删除</a>
                    </th>
                </template>
                <template v-if="file.suffix === 'jpg'||file.suffix === 'png'||file.suffix === 'gif'">
                    <th>
                        <a href="#" onclick="previewImg1(this)" data-url="{{file.absolutePath}}">预览</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="del(this)" data-url="{{file.absolutePath}}">删除</a>
                    </th>
                </template>
                <template v-if="file.suffix === 'txt'">
                    <th><a href="javascript:void(0);" onclick="editFile(this)" data-url="{{file.absolutePath}}">编辑</a>&nbsp&nbsp&nbsp
                        <a href="javascript:void(0);" onclick="appendFile(this)" data-url="{{file.absolutePath}}">追加</a>&nbsp&nbsp&nbsp
                        <a href="javascript:void(0);" onclick="viewFile(this)" data-url="{{file.absolutePath}}">查看</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="rename(this)" data-url="{{file.absolutePath}}">重命名</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="del(this)" data-url="{{file.absolutePath}}">删除</a></th>
                </template>
                <template v-if="file.suffix === 'none'">
                    <th><a href="javascript:void(0);" onclick="editFile(this)" data-url="{{file.absolutePath}}">编辑</a>&nbsp&nbsp&nbsp
                        <a href="javascript:void(0);" onclick="appendFile(this)" data-url="{{file.absolutePath}}">追加</a>&nbsp&nbsp&nbsp
                        <a href="javascript:void(0);" onclick="viewFile(this)" data-url="{{file.absolutePath}}">查看</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="rename(this)" data-url="{{file.absolutePath}}">重命名</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="del(this)" data-url="{{file.absolutePath}}">删除</a></th>
                </template>
                <template v-if="file.suffix === 'avi'||file.suffix === 'mp4'||file.suffix === 'rmvb'">
                    <th>
                        <a href="#" onclick="rename(this)" data-url="{{file.absolutePath}}">重命名</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="previewMov(this)" data-url="{{file.absolutePath}}">播放</a>&nbsp&nbsp&nbsp
                        <a href="#" onclick="del(this)" data-url="{{file.absolutePath}}">删除</a>
                    </th>
                </template>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
export default {
    data () {
        return {
            list:[],
            xpath:"/",
            newFileShow:0,
            paths:null
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>

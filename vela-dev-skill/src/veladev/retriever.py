import os
from langchain_community.vectorstores import Chroma
from langchain_community.embeddings import FastEmbedEmbeddings

class VelaRetriever:
    def __init__(self, db_path="./doc_vector_db"):
        """
        初始化检索器
        :param db_path: 向量数据库路径 (默认为根目录下的 doc_vector_db)
        """
        # 如果是相对路径，转换为绝对路径，基于当前工作目录
        if not os.path.isabs(db_path):
            db_path = os.path.abspath(db_path)
            
        self.embeddings = FastEmbedEmbeddings(model_name="intfloat/multilingual-e5-large")
        
        if not os.path.exists(db_path):
            raise FileNotFoundError(
                f"Vector database not found at {db_path}. "
                "Please run 'python -m veladev.build_index' first."
            )
        
        self.db = Chroma(persist_directory=db_path, embedding_function=self.embeddings)

    def search(self, query: str, k: int = 3, language: str = None):
        """
        搜索相关文档片段
        :param query: 用户问题
        :param k: 返回的片段数量
        :param language: 过滤语言 ('zh' or 'en')
        """
        filter_dict = {}
        if language in ['zh', 'en']:
            filter_dict = {"lang": language}

        docs = self.db.similarity_search(query, k=k, filter=filter_dict if filter_dict else None)
        
        results = []
        for doc in docs:
            results.append({
                "content": doc.page_content,
                "source": doc.metadata.get('source', 'Unknown'),
                "lang": doc.metadata.get('lang', 'unknown')
            })
        return results

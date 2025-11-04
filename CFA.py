import pandas as pd
import numpy as np
import os
import matplotlib.pyplot as plt
import seaborn as sns
from semopy import Model, calc_stats

# 设置 Matplotlib 支持中文显示
plt.rcParams['font.sans-serif'] = ['SimHei']  # 指定默认字体为黑体
plt.rcParams['axes.unicode_minus'] = False  # 解决保存图像是负号'-'显示为方块的问题

# ==============================
# 1. 读取数据
# ==============================
# --- 请确保文件路径正确 ---
file = r"C:\Users\12456\Desktop\初始论文数据.xlsx"
print(f"读取文件： {file}")
df = pd.read_excel(file)

# 自动识别题项列（前86个）
items = df.columns[:86]
df = df[items].dropna()
df.columns = [f"Q{i + 1}" for i in range(len(df.columns))]

print(f"识别到 {len(df.columns)} 个题项列，将它们重命名为 Q1..Q{len(df.columns)}")
print(f"删除含缺失的行后，剩余样本 {len(df)} 行。\n")

# ==============================
# 2. 构建模型语句
# ==============================
model_desc = """
# 测量模型 (Measurement Model)
F1 =~ Q1+Q2+Q3+Q4+Q5+Q6+Q7+Q8+Q9+Q10
F2 =~ Q11+Q12+Q13+Q14+Q15+Q16+Q17+Q18+Q19+Q20
F3 =~ Q21+Q22+Q23+Q24+Q25+Q26+Q27+Q28+Q29+Q30
F4 =~ Q31+Q32+Q33+Q34+Q35+Q36+Q37+Q38+Q39+Q40
F5 =~ Q41+Q42+Q43+Q44+Q45+Q46+Q47+Q48+Q49+Q50
F6 =~ Q51+Q52+Q53+Q54+Q55+Q56+Q57+Q58+Q59+Q60
F7 =~ Q61+Q62+Q63+Q64+Q65+Q66+Q67+Q68+Q69+Q70
F8 =~ Q71+Q72+Q73+Q74+Q75+Q76+Q77+Q78+Q79+Q80+Q81+Q82+Q83+Q84+Q85+Q86
"""

# ==============================
# 3. 拟合模型
# ==============================
print("开始拟合 CFA 模型（semopy）...")
model = Model(model_desc)
model.fit(df)
print("拟合完成。\n")

# ==============================
# 4. 提取参数估计 (强制要求标准化系数)
# ==============================
print("提取参数估计（因子载荷等，强制标准化）...")
# 强制 semopy 计算标准化系数，这在旧版本中可能新增名为 'Std. Est' 或 'Est.Std' 的列
# 我们假定新增列名为 'Std. Est'
try:
    estimates = model.inspect(std_est=True)
except Exception:
    # 如果 std_est=True 不被接受，退回并手动添加警告
    estimates = model.inspect()
    print("警告：model.inspect(std_est=True) 调用失败。热力图可能无法使用标准化系数。")

params_out = r"C:\Users\12456\Desktop\CFA_output\CFA_params_standardized.xlsx"
os.makedirs(os.path.dirname(params_out), exist_ok=True)
estimates.to_excel(params_out, index=False)
print(f"参数估计已保存至：{params_out}\n")

# ==============================
# 5. 拟合度指标计算 (尝试从 stats 或手动计算 SRMR)
# ==============================
print("计算拟合度指标...")
stats_df = calc_stats(model)
stats_row = stats_df.iloc[0]

fit_indices = {
    "Chi-square": stats_row.get("chi2", np.nan),
    "df": stats_row.get("DoF", np.nan),
    "CFI": stats_row.get("CFI", np.nan),
    "TLI": stats_row.get("TLI", np.nan),
    "RMSEA": stats_row.get("RMSEA", np.nan),
    "SRMR": stats_row.get("SRMR", np.nan)
}

# 尝试手动计算 SRMR (应对极旧版本不提供 SRMR 的情况)
if np.isnan(fit_indices["SRMR"]):
    print("警告：calc_stats 未提供 SRMR。尝试手动计算...")
    try:
        # 尝试使用可能的函数名获取隐含协方差矩阵 (Implied Covariance Matrix)
        if hasattr(model, 'get_implied_cov'):
            Sigma_hat = model.get_implied_cov()
        elif hasattr(model, 'calc_implied_cov'):
            Sigma_hat = model.calc_implied_cov()
        elif hasattr(model, 'cov_implied'):
            Sigma_hat = model.cov_implied
        else:
            raise AttributeError("未找到获取隐含协方差矩阵的函数。SRMR计算失败。")

        S = df.cov().values  # 样本协方差矩阵

        # 标准化残差计算 (必要的 SRMR 步骤)
        D = np.diag(np.diag(S) ** (-0.5))
        S_std = D @ S @ D
        Sigma_hat_std = D @ Sigma_hat @ D
        resid = S_std - Sigma_hat_std

        p = S.shape[0]
        n_resid = p * (p - 1) / 2

        srmr = np.sqrt(np.sum(resid[np.tril_indices(p, k=-1)] ** 2) / n_resid)
        fit_indices["SRMR"] = srmr
    except Exception as e:
        print(f"SRMR 手动计算失败： {e}")
        fit_indices["SRMR"] = np.nan

# 计算卡方/自由度
if fit_indices["df"] > 0:
    fit_indices["Chi-square/df"] = fit_indices["Chi-square"] / fit_indices["df"]
else:
    fit_indices["Chi-square/df"] = np.nan

# 保存结果
fit_df = pd.DataFrame(list(fit_indices.items()), columns=["指标", "值"])
fit_out = r"C:\Users\12456\Desktop\CFA_output\CFA_results.xlsx"
fit_df.to_excel(fit_out, index=False)
print(f"拟合度指标已保存至：{fit_out}\n")

# ==============================
# 6. 输出主要指标 (已修复)
# ==============================
print("=== 模型主要拟合指标 ===")
order = ["Chi-square", "df", "Chi-square/df", "CFI", "TLI", "RMSEA", "SRMR"]
for k in order:
    v = fit_indices.get(k)
    if v is not None and not np.isnan(v):
        print(f"{k}: {v:.4f}")
    else:
        print(f"{k}: 缺失")

print("""
模型拟合度解释：
CFI / TLI > 0.90 → 模型拟合良好； > 0.95 → 拟合优
RMSEA < 0.08 → 拟合可接受； < 0.05 → 拟合极好
SRMR < 0.08 → 拟合可接受
""")

# ==============================
# 7. 因子载荷热力图 (已修复)
# ==============================
print("正在生成因子载荷热力图...")
try:
    # 根据上一次输出，尝试寻找标准化系数列名
    std_cols = [col for col in estimates.columns if 'Std' in col or 'Est' in col]

    # 优先尝试 'Std. Est'，因为这是 model.inspect(std_est=True) 在新版本中的输出
    # 如果 estimates 中没有标准化系数，我们退一步使用非标准化系数 'Estimate'
    if 'Std. Est' in estimates.columns:
        loading_col_name = 'Std. Est'
        print(f"使用标准化系数列：'{loading_col_name}' 绘图。")
    elif 'Est.Std' in estimates.columns:
        loading_col_name = 'Est.Std'
        print(f"使用标准化系数列：'{loading_col_name}' 绘图。")
    elif 'Estimate' in estimates.columns:
        loading_col_name = 'Estimate'
        print(f"警告：未找到标准化系数列。使用非标准化系数列：'{loading_col_name}' 绘图。")
    else:
        raise ValueError("无法在参数估计结果中找到任何可用于绘图的系数列。")

    # 筛选出测量模型的路径
    loadings = estimates[estimates['op'] == '=~'][['lval', 'rval', loading_col_name]]

    # 构建透视表用于绘图
    pivot = loadings.pivot(index='rval', columns='lval', values=loading_col_name)

    if pivot.empty:
        raise ValueError("无法生成因子载荷矩阵，可能是因为筛选条件或列名不正确。")

    plt.figure(figsize=(12, 18))  # 调整图像大小以适应86个题项
    sns.heatmap(pivot, cmap="coolwarm", center=0, annot=False, linewidths=.5)
    plt.title(
        f"因子载荷热力图 ({'Standardized' if loading_col_name != 'Estimate' else 'Unstandardized'} Factor Loadings)",
        fontsize=16)
    plt.xlabel("潜变量 (Factors)", fontsize=12)
    plt.ylabel("观测变量 (Items)", fontsize=12)

    heatmap_path = r"C:\Users\12456\Desktop\CFA_output\CFA_factor_loadings_heatmap.png"
    plt.savefig(heatmap_path, bbox_inches='tight', dpi=300)
    print(f"因子载荷热力图已保存：{heatmap_path}")

except Exception as e:
    print(f"因子载荷热力图绘制失败：{e}")

print("\n✅ 全部完成！请前往输出目录查看结果。")
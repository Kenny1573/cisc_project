<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
    <div class="container">



          <route-view></route-view>



      <div class="footer">

        <div class="copyright">
          Copyright &copy; 2020 <a href="http://www.daway.com.cn" target="_blank">江苏航天大为科技股份有限公司</a> 出品
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import RouteView from '@/components/layouts/RouteView'
  import { mixinDevice } from '@/utils/mixin.js'

  export default {
    name: 'UserLayout',
    components: { RouteView },
    mixins: [mixinDevice],
    data () {
      return {}
    },
    mounted () {
      document.body.classList.add('userLayout')
    },
    beforeDestroy () {
      document.body.classList.remove('userLayout')
    }
  }
</script>

<style lang="less" scoped>
  #userLayout.user-layout-wrapper {
    height: 100%;

    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }



    .container {
      width: 100%;
      min-height: 100%;
      background: -webkit-linear-gradient(#163cc8, #395cdd); /* Safari 5.1 - 6.0 */
      background: -o-linear-gradient(#163cc8, #395cdd); /* Opera 11.1 - 12.0 */
      background: -moz-linear-gradient(#163cc8, #395cdd); /* Firefox 3.6 - 15 */
      background: linear-gradient(#163cc8, #395cdd); /* 标准的语法 */
      background-color: #1c41cb;
      color: #666;
      font: 12px/150% Arial, Verdana, "microsoft yahei";
      background-size: 100%;
      padding: 110px 0 144px;
      position: relative;

      a {
        text-decoration: none;
      }

      .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 66px;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, .85);
            font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            font-weight: 600;
            position: relative;
            top: 9px;
          }
        }

        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 400px;
        width: 1100px;
        margin: 200px auto;
      }

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;

          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;

            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }

        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }
      }
    }
  }
</style>
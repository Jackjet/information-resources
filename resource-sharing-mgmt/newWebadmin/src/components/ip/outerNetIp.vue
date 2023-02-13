<template>
    <remote-js :src="src"></remote-js>
</template>
<script>
    export default {
        name: 'OuterIp',
        data() {
            return {
                src: ''
            }
        },
        components: {
            'remote-js': {
                render(createElement) {
                    return createElement('script', {
                        attrs: { type: 'text/javascript', src: this.src || 'http://pv.sohu.com/cityjson?ie=utf-8' }
                    })
                },
                props: {
                    src: { type: String, required: true }
                }
            }
        }
    }
</script>
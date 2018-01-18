const gulp = require("gulp");
const clean = require('gulp-clean');
const merge = require('merge-stream');
const sourcemaps = require('gulp-sourcemaps');
const ts = require('gulp-typescript');

gulp.task('default', ['copy'], () => {
    return gulp.src(['src/**/*.ts', 'src/**/*.tsx'])
        .pipe(sourcemaps.init())
        .pipe(ts({
            jsx: 'react',
            module: 'amd',
            moduleResolution: 'node',
            out: 'app.js',
            sourceMap: true,
            removeComments: true,
            target: 'es5'
        })).js.pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('build/lib'));
});

gulp.task('copy', ['clean'], () => {
    const html = gulp.src(['src/html/*.*'])
        .pipe(gulp.dest('build'));
    const bootstrap = gulp.src('node_modules/bootstrap/dist/**')
        .pipe(gulp.dest('build/lib/bootstrap'));
    const libs = gulp.src([
        'node_modules/jquery/dist/jquery.min.js',
        'node_modules/popper.js/dist/umd/popper.min.js',
        'node_modules/requirejs/require.js',
        'node_modules/react/umd/react.production.min.js',
        'node_modules/react-dom/umd/react-dom.production.min.js',
        'node_modules/prismjs/prism.js',
        'node_modules/prismjs/components/prism-json.min.js',
        'node_modules/prismjs/components/prism-python.min.js',
        'node_modules/prismjs/themes/prism.css',
        'node_modules/font-awesome/css/font-awesome.min.css',
    ]).pipe(gulp.dest('build/lib'));
    const fonts = gulp.src('node_modules/font-awesome/fonts/**')
        .pipe(gulp.dest('build/fonts'));
    return merge(html, bootstrap, libs, fonts);
});

gulp.task('clean', () => {
    return gulp.src(['build'], {
        read: false
    }).pipe(clean({ force: true }));
});